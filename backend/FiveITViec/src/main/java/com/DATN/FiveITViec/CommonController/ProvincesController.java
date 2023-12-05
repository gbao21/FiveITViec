package com.DATN.FiveITViec.CommonController;

import com.DATN.FiveITViec.model.Province;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProvincesController {

    @GetMapping("/provinces")
    public List<Province> getProvinces() {
        List<Province> provinces = new ArrayList<>();
        provinces.add(new Province(1L, "An Giang"));
        provinces.add(new Province(2L, "Bà Rịa – Vũng Tàu"));
        provinces.add(new Province(3L, "Bắc Giang"));
        provinces.add(new Province(4L, "Bắc Kạn"));
        provinces.add(new Province(5L, "Bạc Liêu"));
        provinces.add(new Province(6L, "Bắc Ninh"));
        provinces.add(new Province(7L, "Bến Tre"));
        provinces.add(new Province(8L, "Bình Định"));
        provinces.add(new Province(9L, "Bình Dương"));
        provinces.add(new Province(10L, "Bình Phước"));
        provinces.add(new Province(11L, "Bình Thuận"));
        provinces.add(new Province(12L, "Cà Mau"));
        provinces.add(new Province(13L, "Cần Thơ"));
        provinces.add(new Province(14L, "Cao Bằng"));
        provinces.add(new Province(15L, "Đà Nẵng"));
        provinces.add(new Province(16L, "Đắk Lắk"));
        provinces.add(new Province(17L, "Đắk Nông"));
        provinces.add(new Province(18L, "Điện Biên"));
        provinces.add(new Province(19L, "Đồng Nai"));
        provinces.add(new Province(20L, "Đồng Tháp"));
        provinces.add(new Province(21L, "Gia Lai"));
        provinces.add(new Province(22L, "Hà Giang"));
        provinces.add(new Province(23L, "Hà Nam"));
        provinces.add(new Province(24L, "Hà Nội"));
        provinces.add(new Province(25L, "Hà Tĩnh"));
        provinces.add(new Province(26L, "Hải Dương"));
        provinces.add(new Province(27L, "Hải Phòng"));
        provinces.add(new Province(28L, "Hậu Giang"));
        provinces.add(new Province(29L, "Hòa Bình"));
        provinces.add(new Province(30L, "Hưng Yên"));
        provinces.add(new Province(31L, "Khánh Hòa"));
        provinces.add(new Province(32L, "Kiên Giang"));
        provinces.add(new Province(33L, "Kon Tum"));
        provinces.add(new Province(34L, "Lai Châu"));
        provinces.add(new Province(35L, "Lâm Đồng"));
        provinces.add(new Province(36L, "Lạng Sơn"));
        provinces.add(new Province(37L, "Lào Cai"));
        provinces.add(new Province(38L, "Long An"));
        provinces.add(new Province(39L, "Nam Định"));
        provinces.add(new Province(40L, "Nghệ An"));
        provinces.add(new Province(41L, "Ninh Bình"));
        provinces.add(new Province(42L, "Ninh Thuận"));
        provinces.add(new Province(43L, "Phú Thọ"));
        provinces.add(new Province(44L, "Phú Yên"));
        provinces.add(new Province(45L, "Quảng Bình"));
        provinces.add(new Province(46L, "Quảng Nam"));
        provinces.add(new Province(47L, "Quảng Ngãi"));
        provinces.add(new Province(48L, "Quảng Ninh"));
        provinces.add(new Province(49L, "Quảng Trị"));
        provinces.add(new Province(50L, "Sóc Trăng"));
        provinces.add(new Province(51L, "Sơn La"));
        provinces.add(new Province(52L, "Tây Ninh"));
        provinces.add(new Province(53L, "Thái Bình"));
        provinces.add(new Province(54L, "Thái Nguyên"));
        provinces.add(new Province(55L, "Thanh Hóa"));
        provinces.add(new Province(56L, "Thừa Thiên Huế"));
        provinces.add(new Province(57L, "Tiền Giang"));
        provinces.add(new Province(58L, "Thành phố Hồ Chí Minh"));
        provinces.add(new Province(59L, "Trà Vinh"));
        provinces.add(new Province(60L, "Tuyên Quang"));
        provinces.add(new Province(61L, "Vĩnh Long"));
        provinces.add(new Province(62L, "Vĩnh Phúc"));
        provinces.add(new Province(63L, "Yên Bái"));
        return provinces;
    }

}
