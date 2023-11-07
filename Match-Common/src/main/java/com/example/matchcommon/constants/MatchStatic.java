package com.example.matchcommon.constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MatchStatic {
    public static final String BEARER = "Bearer ";
    public static final String BASIC = "Basic ";

    public static final String BILL_TEST = "카드 확인 결제";

    public static final String BILL_TEST_CANCEL = "카드 확인 결제 취소";

    public static final String REGULAR = "REGULAR-";

    public static final String ONE_TIME = "ONE_TIME-";

    public static final String BILL = "BILL-";

    public static final String DELETE  = "DELETE-";

    public static final String FIRST_TIME = "T00:00:00";
    public static final String LAST_TIME = "T23:59:59";

    public static final String BASE_PROFILE = "https://match-image.s3.ap-northeast-2.amazonaws.com/profile.png";

    public static final String MATCH_PROFILE = "https://match-image.s3.ap-northeast-2.amazonaws.com/profile.png";

    public static final String MATCH_NAME = "매치";

    public static final String PORT_ONE = "port_one";

    public static final String EVENT_S3_DIR = "event";

    public static final String NOTICE_S3_DIR = "notice";

    public static final String PAYMENT_LOG = "Payment userId: %d, orderId: %s, bid: %s, amount: %d원, projectId: %d";
    public static final String PAYMENT_DATE_FORMAT = "yy.MM.dd.HH:mm";
    public static final String PAYMENT_ALERT_START = "정기 결제 스케줄러 시작";
    public static final String PAYMENT_ALERT_FINISH = "정기 결제 스케줄러 종료";
    public static final String PAYMENT_RETRY_START = "정기 결제 실패 한 리스트 스케줄러가 시작";
    public static final String PAYMENT_RETRY_FINISH = "실패한 정기 결제 스케줄러가 종료";
    public static final String REGULAR_PAYMENT = "REGULAR";

    public static final String PAYMENT_LOG_INFO = "Not pay Day of Month userId : %s bid : %s amount : %d원 projectId : %s payId : %s";


    public static final String SUCCESS_PAYMENT_LOG = "Success Payment userId : %s orderId : %s bid : %s amount : %d원 projectId : %s";

    public static final String FAILED_PAYMENT_LOG = "Fail Payment regularPaymentId : %s Reason : %s";

    public static final String RETRY_PAYMENT = "RETRY";

    public static final String PAY_TITLE = getCurrentDateFormatted() + " MATCH 기부금 정기 결제";
    private static String getCurrentDateFormatted() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(PAYMENT_DATE_FORMAT));
    }

}
