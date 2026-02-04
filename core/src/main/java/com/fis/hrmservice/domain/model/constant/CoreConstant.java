package com.fis.hrmservice.domain.model.constant;

/** Domain constants - pure Java, no framework dependencies. */
public final class CoreConstant {

  private CoreConstant() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  // === MIME Types ===
  public static final String MIME_TYPE_PDF = "application/pdf";
  public static final String MIME_TYPE_DOCX =
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
  public static final String MIME_TYPE_PNG = "image/png";
  public static final String MIME_TYPE_JPG = "image/jpeg";

  // === File Extensions (regex patterns) ===
  public static final String FILE_EXT_PDF = "\\.pdf$";
  public static final String FILE_EXT_DOCX = "\\.docx$";
  public static final String FILE_EXT_PNG = "\\.png$";
  public static final String FILE_EXT_JPG = "\\.(jpg|jpeg)$";

  // === File Size Limits ===
  public static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10MB
  public static final long MAX_FILE_SIZE_MB = 10;

  // === Validation Patterns ===
  public static final String EMAIL_FORMAT = "^[A-Za-z][A-Za-z0-9._%+-]*@fpt\\.com$";
  public static final String ID_NUMBER_FORMAT = "^(00[1-9]|0[1-8][0-9]|09[0-6])[0-9]{9}$";
  public static final String PHONE_NUMBER_FORMAT =
      "^(0)(32|33|34|35|36|37|38|39|86|96|97|98|81|82|83|84|85|88|91|94|70|76|77|78|79|89|90|93|52|56|58|92|59|99|55|87)\\d{7}$";

  // === Date Formats ===
  public static final String DATE_FORMAT = "dd/MM/yyyy";
  public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
}
