import React, { ChangeEvent, useEffect, useState } from "react";
import { SpinnerLoading } from "../../utils/SpinnerLoading";
import { Page404 } from "../../errors/Page404";
import { Pagination } from "../../utils/Pagination";
import { BlogModel } from "../../../models/BlogModel";
import ReactQuill from "react-quill";
import { useTranslation } from "react-i18next";
import { BarChart } from "@mui/x-charts/BarChart";
import { CSVLink } from "react-csv";

export const WaitingBlogAdminPage = () => {
  const { t } = useTranslation();
  const [isLoading, setIsLoading] = useState(true);
  const [httpError, setHttpError] = useState(null);
  const [blogs, setBlogs] = useState<BlogModel[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [blogsPerPage] = useState(5);
  const [totalBlogs, setTotalBlogs] = useState(0);
  const [totalPage, setTotalPage] = useState(0);
  const [searchUrl, setSearchUrl] = useState("");
  const [search, setSearch] = useState("");
  const approval: string = "WAITING";
  const [editingBlog, setEditingBlog] = useState<BlogModel | null>(null);
  const [edit, setEdit] = useState(false);
  const [updated, setUpdated] = useState(false);
  const [showToast, setShowToast] = useState(false);
  const [message, setMessage] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [searchStatus, setSearchStatus] = useState("");
  const [EditorHtml, setEditorHtml] = useState("");
  const [totalWaitingBlogByMonth, setTotalWaitingBlogByMonth] = useState([0]);
  const [year, setYear] = useState("2023");
  const showToastMessage = (message: string) => {
    setMessage(message);
    setShowToast(true);
    setTimeout(() => setShowToast(false), 3000);
  };



  const token: any = localStorage.getItem("jwt_token");

  const fetchAllBlogs = async () => {
    try {
      let baseUrlForBlog = "";
      if (searchUrl === "") {
        baseUrlForBlog = `http://localhost:8080/auth/admin/getAllBlogs?blogTitle=${search}&approval=${approval}&status=DISABLE&startDate=${startDate}&endDate=${endDate}&page=${currentPage - 1
          }&size=${blogsPerPage}`;
      } else {
        let searchWithPage = searchUrl.replace(
          "<currentPage>",
          `${currentPage - 1}`
        );
        baseUrlForBlog = searchWithPage;
      }

      const response = await fetch(baseUrlForBlog, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        console.log(data);
        const loadedBlogs: BlogModel[] = [];
        data.content.forEach((blogData: any) => {
          const blog = new BlogModel(
            blogData.blogId,
            blogData.userId,
            blogData.blogTitle,
            blogData.blogContent,
            blogData.blogImg,
            blogData.author,
            blogData.status,
            blogData.approval,
            blogData.createdAt,
            blogData.createdBy,
            blogData.updatedAt,
            blogData.updatedBy
          );

          loadedBlogs.push(blog);
        });

        setBlogs(loadedBlogs);
        if (updated) {
          setCurrentPage(1);
          setUpdated(false);
        }
        setTotalBlogs(data.totalElements);
        setTotalPage(data.totalPages);
        setIsLoading(false);
      } else {
        throw new Error("Request failed");
      }
    } catch (error: any) {
      setHttpError(error.message);
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchAllBlogs();
  }, [currentPage, searchUrl, updated]);

  const searchHandleChange = (
    searchKey: string,
    searchStartDate: string,
    searchEndDate: string,
    searchStatus: string
  ) => {
    setCurrentPage(1);
    setEdit(false);
    setEditingBlog(null);

    let newURL = `http://localhost:8080/auth/admin/getAllBlogs?blogTitle=${search}&approval=${approval}&status=DISABLE&startDate=${startDate}&endDate=${endDate}&page=<currentPage>&size=${blogsPerPage}`;
    setSearchUrl(newURL);
  };

  const handleEditBlog = (Blog: BlogModel) => {
    setEditingBlog(Blog);
    setEdit(true);
    setEditorHtml(Blog.blogContent);
    window.scrollTo({
      top: 0,
      behavior: "smooth", // for smooth scrolling
    });
  };

  const handleCancel = () => {
    setEditingBlog(null);
    setEdit(false);
    setEditorHtml("");
  };

  const approveBlog = async () => {
    if (editingBlog) {
      setIsLoading(true);
      try {
        const response = await fetch(
          `http://localhost:8080/auth/admin/updateBlogStatus?blogId=${editingBlog.blogId.toString()}&approval=${editingBlog.approval
          }&status=${editingBlog.status}`,
          {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (response.ok) {
          setUpdated(true);
          setIsLoading(false);
          showToastMessage(t("showToastMessage.updateSuccess"));
          handleCancel();
        } else {
          showToastMessage(t("showToastMessage.updateFailed"));
        }
      } catch (error) {
        console.error("Error approve status:", error);
      }
    }
  };
  useEffect(() => {
    const fetchTotalCandidateByMonth = async () => {
      try {
        const baseUrl = `http://localhost:8080/auth/admin/getTotalWaitingBlogByMonth?year=${year}`;
        const response = await fetch(baseUrl, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`, // Replace with your actual authorization token
          },
        });
        if (response.ok) {
          const data = await response.json();
          setTotalWaitingBlogByMonth(data.waitingBlogMap);
        } else {
          console.error("Failed to fetch");
        }
      } catch (error: any) {
        setHttpError(error.message);
      }
    };
    fetchTotalCandidateByMonth();
  }, [year]);

  const candidateData: { month: number; value: number }[] = [];
  for (const key in totalWaitingBlogByMonth) {
    if (totalWaitingBlogByMonth.hasOwnProperty(key)) {
      const item = {
        month: parseInt(key),
        value: totalWaitingBlogByMonth[key],
      };
      candidateData.push(item);
    }
  }
  const xLabels = [
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sep",
    "Oct",
    "Nov",
    "Dec",
  ];

  const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

  if (isLoading) {
    return <SpinnerLoading />;
  }

  if (httpError) {
    return <Page404 error={httpError} />;
  }
  return (
    <>
      {edit && editingBlog && (
        <div className="container mt-4">
          <div className="row">
            <div className="col-md-12">
              <div className="card">
                <div className="card-body">
                  <div className="text-center mb-4">
                    <label htmlFor="fileInput" className="file-input-label">
                      <div
                        className="image-container"
                        style={{ position: "relative" }}
                      >
                        <img
                          src={
                            editingBlog.blogImg ||
                            "https://res.cloudinary.com/dzqoi9laq/image/upload/v1699419757/logoo_pyz2sp.png"
                          }
                          alt="Candidate"
                          className="img-fluid rounded-circle"
                          style={{
                            width: "10em",
                            objectFit: "fill",
                            height: "150px",
                          }}
                        />
                      </div>
                    </label>
                  </div>

                  <form
                    method="PUT"
                    className="col-md-12 d-flex align-items-center justify-content-center flex-column"
                  >
                    <div className="col-md-8">
                      <div className="mb-3 ms-2 me-2">
                        <label
                          htmlFor="title"
                          className="form-label fw-bold ms-2"
                        >
                          {t("placeholders.title")}
                        </label>
                        <input
                          type="text"
                          name="title"
                          id="title"
                          value={editingBlog.blogTitle || ""}
                          className="form-control"
                          placeholder="Title"
                          readOnly
                        />
                      </div>
                      <div className="mb-3 ms-2 me-2">
                        <label
                          htmlFor="blogContent"
                          className="form-label fw-bold ms-2"
                        >
                          {t("placeholders.blogContent")}
                        </label>
                        <ReactQuill
                          value={EditorHtml}
                          onChange={setEditorHtml}
                          style={{ height: "80px" }}
                          readOnly
                        />
                      </div>
                      <div className="mb-3 mt-5 ms-2 me-2">
                        <label
                          htmlFor="author"
                          className="form-label fw-bold ms-2"
                        >
                          {t("placeholders.author")}
                        </label>
                        <input
                          type="text"
                          name="author"
                          value={editingBlog.author}
                          className="form-control"
                          placeholder="Author"
                          readOnly
                        />
                      </div>
                    </div>
                    <div className="text-center">
                      <button
                        type="button"
                        className="btn btn-success"
                        onClick={approveBlog}
                      >
                        {t("btn.btnApproved")}
                      </button>
                      <button
                        type="submit"
                        className="btn btn-danger ms-3"
                        onClick={handleCancel}
                      >
                        {t("btn.btnCancel")}
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
      {!edit && !editingBlog && (
        <div className="container mt-4">
          <div className="row justify-content-center">
            <div className="col-md-9">
              <div className="d-flex align-items-center justify-content-between mb-4">
                <h6 className="mb-0 fw-bold text-success">{t("dashboard.statisticsWaitingBlog")}</h6>
                <div className="col-3">
                  <select
                    className="form-control"
                    id="selectMonth"
                    value={year}
                    onChange={(e) => setYear(e.target.value)}
                  >
                    <option value="2018">2018</option>
                    <option value="2019">2019</option>
                    <option value="2020">2020</option>
                    <option value="2021">2021</option>
                    <option value="2022">2022</option>
                    <option value="2023">2023</option>
                    <option value="2024">2024</option>
                  </select>
                </div>
              </div>
              <div className="card">
                <BarChart
                  xAxis={[
                    {
                      id: "barCategories",
                      data: xLabels,
                      scaleType: "band",
                    },
                  ]}
                  series={[
                    {
                      data: candidateData.map((item) => item.value),
                      label: t("dashboard.waitingBlog"),
                      color: "#198754",

                    },
                  ]}
                  // width={800}
                  height={450}
                />
              </div>
            </div>
          </div>
        </div>
      )}

      <div className="container-fluid pt-4 px-4">
        <div className="row g-4">
          <div className="col-12">
            <div className="bg-light rounded h-100 p-4">
              {/* Search form */}
              <form
                onSubmit={(e) => {
                  e.preventDefault();
                  searchHandleChange(search, startDate, endDate, searchStatus);
                }}
              >
                <div className="form-row">
                  <div className="row">
                    <div className="col-md-4 col-lg-3 mb-3">
                      <input
                        type="text"
                        className="form-control"
                        id="keyword"
                        placeholder="Title"
                        onChange={(e) => setSearch(e.target.value)}
                      />
                    </div>
                    <div className="col-md-2 col-lg-3 mb-3">
                      <input
                        type="date"
                        className="form-control"
                        id="keyword"
                        placeholder="Keyword"
                        onChange={(e) => setStartDate(e.target.value)}
                      />
                    </div>
                    <div className="col-md-2 col-lg-3 mb-3">
                      <input
                        type="date"
                        className="form-control"
                        id="keyword"
                        placeholder="Keyword"
                        onChange={(e) => setEndDate(e.target.value)}
                      />
                    </div>

                    <div className="col-md-2 col-lg-2 text-center mb-3">
                      <button
                        className="btn btn-success btn-block"
                        type="button"
                        onClick={() =>
                          searchHandleChange(
                            search,
                            startDate,
                            endDate,
                            searchStatus
                          )
                        }
                      >
                        {t("searchForm.searchBtn")}
                      </button>
                    </div>
                    {blogs.length > 0 && (
                      <div className="col-md-2 col-lg-1 mb-3">
                        <CSVLink
                          className="btn btn-success btn-block"
                          data={blogs}
                          filename="Waiting Blogs"
                          target="_blank"
                        >
                          Excel
                        </CSVLink>
                      </div>
                    )}
                  </div>
                </div>
              </form>
              <>
                {blogs.length > 0 ? (
                  <div className="table-responsive">

                    <table className="table">
                      <thead className="">
                        <tr>
                          <th scope="col">#</th>
                          <th scope="col">{t("table.title")}</th>
                          <th scope="col">{t("table.author")}</th>
                          <th scope="col">{t("table.img")}</th>
                          <th scope="col">{t("table.createdAt")}</th>
                          <th scope="col">{t("table.status")}</th>
                          <th scope="col">{t("table.approval")}</th>
                          <th scope="col">{t("table.action")}</th>
                        </tr>
                      </thead>
                      <tbody className="">
                        {blogs.map((blog, index) => (
                          <tr
                            key={blog.blogId}
                            style={{ verticalAlign: "middle" }}
                          >
                            <th scope="row">{index + 1}</th>
                            <td>{blog.blogTitle}</td>
                            <td>{blog.author}</td>
                            <td>
                              {blog.blogImg && (
                                <img
                                  src={blog.blogImg}
                                  alt={`Profile image of ${blog.blogTitle}`}
                                  className="text-start"
                                  style={{
                                    maxWidth: "80px",
                                    maxHeight: "100px",
                                  }}
                                />
                              )}
                            </td>
                            <td>{blog.createdAt}</td>
                            <td>{blog.status}</td>
                            <td>{blog.approval}</td>
                            <td style={{ minWidth: '100px' }}>
                              <button
                                type="button"
                                className="btn btn-success"
                                onClick={() => handleEditBlog(blog)}
                              >
                                <i className="fa fa-pencil-alt"></i> {t('btn.btnEdit')}
                              </button>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                ) : (
                  <div className="text-center">
                    <div className="background">{t("admin.noBlog")}</div>
                    <div>
                      <img
                        src="/assets/img/sorry.png"
                        className="img-fluid w-50"
                      />
                    </div>
                  </div>
                )}
              </>
            </div>
          </div>
        </div>
      </div>

      {totalPage >= 2 && (
        <div className="mt-3">
          <Pagination
            currentPage={currentPage}
            totalPage={totalPage}
            paginate={paginate}
          />
        </div>
      )}

      {showToast === true && (

        <div className="position-fixed bottom-0 end-0 p-0" style={{ zIndex: 5 }}>
          <div
            className={`toast ${showToast ? "show" : ""}`}
            role="alert"
            aria-live="assertive"
            aria-atomic="true"
          >
            <div className="toast-header bg-success text-white ">
              <strong className="me-auto">{t('showToastMessage.status')}</strong>
              <button
                type="button"
                className="btn-close"
                data-bs-dismiss="toast"
                onClick={() => setShowToast(false)}
              ></button>
            </div>
            <div className="toast-body">{message}</div>
          </div>
        </div>
      )}
    </>
  );
};
