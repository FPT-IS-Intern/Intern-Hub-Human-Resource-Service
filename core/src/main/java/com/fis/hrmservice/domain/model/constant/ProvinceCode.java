package com.fis.hrmservice.domain.model.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ProvinceCode {

  private ProvinceCode() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  private static final Map<Integer, String> PROVINCE_CODE_MAP;

  static {
    Map<Integer, String> map = new HashMap<>();
    map.put(1, "Thành phố Hà Nội"); // 01
    map.put(4, "Tỉnh Cao Bằng"); // 04
    map.put(8, "Tỉnh Tuyên Quang"); // 08
    map.put(11, "Tỉnh Điện Biên"); // 11
    map.put(12, "Tỉnh Lai Châu"); // 12
    map.put(14, "Tỉnh Sơn La"); // 14
    map.put(15, "Tỉnh Lào Cai"); // 15
    map.put(19, "Tỉnh Thái Nguyên"); // 19
    map.put(20, "Tỉnh Lạng Sơn"); // 20
    map.put(22, "Tỉnh Quảng Ninh"); // 22
    map.put(24, "Tỉnh Bắc Ninh"); // 24
    map.put(25, "Tỉnh Phú Thọ"); // 25
    map.put(31, "Thành phố Hải Phòng"); // 31
    map.put(33, "Tỉnh Hưng Yên"); // 33
    map.put(37, "Tỉnh Ninh Bình"); // 37
    map.put(38, "Tỉnh Thanh Hóa"); // 38
    map.put(40, "Tỉnh Nghệ An"); // 40
    map.put(42, "Tỉnh Hà Tĩnh"); // 42
    map.put(44, "Tỉnh Quảng Trị"); // 44
    map.put(46, "Thành phố Huế"); // 46
    map.put(48, "Thành phố Đà Nẵng"); // 48
    map.put(51, "Tỉnh Quảng Ngãi"); // 51
    map.put(52, "Tỉnh Gia Lai"); // 52
    map.put(56, "Tỉnh Khánh Hòa"); // 56
    map.put(66, "Tỉnh Đắk Lắk"); // 66
    map.put(68, "Tỉnh Lâm Đồng"); // 68
    map.put(75, "Tỉnh Đồng Nai"); // 75
    map.put(79, "Thành phố Hồ Chí Minh"); // 79
    map.put(80, "Tỉnh Tây Ninh"); // 80
    map.put(82, "Tỉnh Đồng Tháp"); // 82
    map.put(86, "Tỉnh Vĩnh Long"); // 86
    map.put(91, "Tỉnh An Giang"); // 91
    map.put(92, "Thành phố Cần Thơ"); // 92
    map.put(96, "Tỉnh Cà Mau"); // 96

    PROVINCE_CODE_MAP = Collections.unmodifiableMap(map);
  }

  public static String getProvinceName(Integer code) {
    return PROVINCE_CODE_MAP.get(code);
  }

  public static Integer getProvinceCode(String provinceName) {
    for (Map.Entry<Integer, String> entry : PROVINCE_CODE_MAP.entrySet()) {
      if (entry.getValue().equals(provinceName)) {
        return entry.getKey();
      }
    }
    return null;
  }
}
