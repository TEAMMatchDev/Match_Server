package com.example.matchcommon.exception.errorcode;

import com.example.matchcommon.annotation.ExplainError;
import com.example.matchcommon.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum NicePayErrorCode implements BaseErrorCode {

    /**
     * 잘못된 요청
     */
    CARD_PAYMENT_SUCCESS(HttpStatus.OK, "3001", "카드 결제 성공"),
    CARD_NUMBER_ERROR(HttpStatus.BAD_REQUEST, "3011", "카드번호 오류"),
    CARD_MERCHANT_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "3012", "카드가맹점 정보 미확인"),
    CARD_MERCHANT_NOT_STARTED(HttpStatus.BAD_REQUEST, "3013", "카드 가맹점 개시 안됨"),
    CARD_MERCHANT_INFO_ERROR(HttpStatus.BAD_REQUEST, "3014", "카드가맹점 정보 오류"),
    CARD_EXPIRY_DATE_ERROR(HttpStatus.BAD_REQUEST, "3021", "유효기간 오류"),
    INSTALLMENT_MONTHS_ERROR(HttpStatus.BAD_REQUEST, "3022", "할부개월오류"),
    INSTALLMENT_MONTHS_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "3023", "할부개월 한도 초과"),
    INTEREST_FREE_INSTALLMENTS_CARD_ERROR(HttpStatus.BAD_REQUEST, "3031", "무이자할부 카드 아님"),
    INTEREST_FREE_INSTALLMENTS_MONTHS_ERROR(HttpStatus.BAD_REQUEST, "3032", "무이자할부 불가 개월수"),
    INTEREST_FREE_INSTALLMENTS_MERCHANT_ERROR(HttpStatus.BAD_REQUEST, "3033", "무이자할부 가맹점 아님"),
    INTEREST_FREE_INSTALLMENTS_TYPE_NOT_SET(HttpStatus.BAD_REQUEST, "3034", "무이자할부 구분 미설정"),
    AMOUNT_ERROR(HttpStatus.BAD_REQUEST, "3041", "금액 오류(1000원 미만 신용카드 승인 불가)"),
    UNREGISTERED_FOREIGN_CARD_MERCHANT(HttpStatus.BAD_REQUEST, "3051", "해외카드 미등록 가맹점"),
    CURRENCY_CODE_ERROR(HttpStatus.BAD_REQUEST, "3052", "통화코드 오류"),
    UNCONFIRMED_OVERSEAS_CARD(HttpStatus.BAD_REQUEST, "3053", "확인 불가 해외카드"),
    EXCHANGE_RATE_CONVERSION_ERROR(HttpStatus.BAD_REQUEST, "3054", "환률전환오류"),
    DOLLAR_APPROVAL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "3055", "인증시 달러승인 불가"),
    DOMESTIC_CARD_DOLLAR_APPROVAL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "3056", "국내카드 달러승인불가"),
    UNCONFIRMED_CARD(HttpStatus.BAD_REQUEST, "3057", "인증 불가카드"),
    KOOKMIN_CARD_INTERNET_SAFE_PAYMENT_MERCHANT(HttpStatus.BAD_REQUEST, "3061", "국민카드 인터넷안전결제 적용 가맹점"),
    CREDIT_CARD_APPROVAL_NUMBER_ERROR(HttpStatus.BAD_REQUEST, "3062", "신용카드 승인번호 오류"),
    NOT_PURCHASE_REQUEST_MERCHANT(HttpStatus.BAD_REQUEST, "3071", "매입요청 가맹점 아님"),
    MISMATCHED_PURCHASE_REQUEST_TID(HttpStatus.BAD_REQUEST, "3072", "매입요청 TID 정보 불일치"),
    PURCHASE_REQUEST_ALREADY_MADE(HttpStatus.BAD_REQUEST, "3073", "기매입 거래"),
    CARD_BALANCE_VALUE_ERROR(HttpStatus.BAD_REQUEST, "3081", "카드 잔액 값 오류"),
    AFFILIATE_CARD_NOT_SUPPORTED_MERCHANT(HttpStatus.BAD_REQUEST, "3091", "제휴카드 사용불가 가맹점"),
    CARD_COMPANY_FAILURE_RESPONSE(HttpStatus.BAD_REQUEST, "3095", "카드사 실패 응답"),
    ACCOUNT_TRANSFER_SUCCESS(HttpStatus.OK, "4000", "계좌이체 결제 성공"),
    BANK_NOT_AVAILABLE_FOR_MEMBER_COMPANY(HttpStatus.BAD_REQUEST, "4001", "회원사 서비스 불가 은행"),
    WITHDRAWAL_DATE_MISMATCH(HttpStatus.BAD_REQUEST, "4002", "출금일자 불일치"),
    WITHDRAWAL_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "4003", "출금요청금액 불일치"),
    TRANSACTION_NUMBER_TID_MISMATCH(HttpStatus.BAD_REQUEST, "4004", "거래번호(TID) 불일치"),
    RESPONSE_INFO_MISMATCH(HttpStatus.BAD_REQUEST, "4005", "회신 정보 불일치"),
    ACCOUNT_TRANSFER_APPROVAL_NUMBER_ERROR(HttpStatus.BAD_REQUEST, "4006", "계좌이체 승인번호 오류"),
    BANK_SYSTEM_SERVICE_STOPPED(HttpStatus.BAD_REQUEST, "4007", "은행 시스템 서비스 중단"),
    VIRTUAL_ACCOUNT_ISSUE_SUCCESS(HttpStatus.OK, "4100", "가상계좌 발급 성공"),
    VIRTUAL_ACCOUNT_DEPOSIT_SUCCESS(HttpStatus.OK, "4110", "가상계좌 입금 성공"),
    VIRTUAL_ACCOUNT_OVERPAYMENT_CHECK_REGISTER_SUCCESS(HttpStatus.OK, "4120", "가상계좌 과오납체크 등록 성공"),
    VIRTUAL_ACCOUNT_MAX_TRANSACTION_AMOUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "4101", "가상계좌 최대거래금액 초과"),
    VIRTUAL_ACCOUNT_DEPOSIT_DATE_ERROR(HttpStatus.BAD_REQUEST, "4102", "가상계좌 입금예정일 오류"),
    VIRTUAL_ACCOUNT_DEPOSIT_TIME_ERROR(HttpStatus.BAD_REQUEST, "4103", "가상계좌 입금예정시간 오류"),
    VIRTUAL_ACCOUNT_INFORMATION_ERROR(HttpStatus.BAD_REQUEST, "4104", "가상계좌 정보 오류"),
    VIRTUAL_ACCOUNT_BULK_ACCOUNT_NOT_ALLOWED_INDIVIDUAL_MERCHANT(HttpStatus.BAD_REQUEST, "4105", "가상계좌 벌크계좌 사용불가.(건별계좌 사용 가맹점)"),
    VIRTUAL_ACCOUNT_BULK_ACCOUNT_NOT_ALLOWED_BULK_MERCHANT(HttpStatus.BAD_REQUEST, "4106", "가상계좌 벌크계좌 사용불가.(벌크계좌 사용 가맹점)"),
    VIRTUAL_ACCOUNT_ACCOUNT_NUMBER_NOT_ENTERED(HttpStatus.BAD_REQUEST, "4107", "가상계좌 계좌번호 미입력 오류"),
    VIRTUAL_ACCOUNT_POOL_MERCHANT_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "4108", "가상계좌 Pool 가맹점 미등록 오류"),
    VIRTUAL_ACCOUNT_ACCOUNT_IN_DEPOSIT_WAITING_STATE(HttpStatus.BAD_REQUEST, "4109", "해당 계좌는 입금대기상태(다른 계좌 사용요망)"),
    VIRTUAL_ACCOUNT_POOL_MERCHANT_ACCOUNT_TYPE_SETTING_ERROR(HttpStatus.BAD_REQUEST, "4111", "가상계좌 Pool 가맹점 계좌형태 설정 오류"),
    VIRTUAL_ACCOUNT_BULK_ACCOUNT_MAX_CART_COUNT(HttpStatus.BAD_REQUEST, "4112", "벌크계좌의 경우 장바구니 개수를 10개 이내로 제한"),
    VIRTUAL_ACCOUNT_CART_MERCHANT_ORDER_NUMBER_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "4113", "장바구니 상점주문번호(MOID) 포맷 오류 (MID(10)+YYMMDD(6)+일련번호(4))"),
    VIRTUAL_ACCOUNT_CART_MERCHANT_ORDER_NUMBER_FORMAT_ERROR2(HttpStatus.BAD_REQUEST, "4114", "장바구니 상점주문번호(MOID) 포맷 오류 (YYMMDD(6)+일련번호(max_size 14))"),
    VIRTUAL_ACCOUNT_ACCOUNT_NUMBER_REUSE_PROHIBITED(HttpStatus.BAD_REQUEST, "4115", "해당 계좌번호는 6개월 이내 재사용 금지"),
    VIRTUAL_ACCOUNT_ACCOUNT_NUMBER_GENERATION_ATTEMPT_EXCEEDED(HttpStatus.BAD_REQUEST, "4116", "가상계좌 계좌번호 채번시도 횟수초과 오류(잠시후 재시도 요망)"),
    VIRTUAL_ACCOUNT_ISSUE_FAILURE(HttpStatus.BAD_REQUEST, "4117", "가상계좌 발급 실패"),
    VIRTUAL_ACCOUNT_ACCOUNT_NUMBER_SUSPENDED(HttpStatus.BAD_REQUEST, "4118", "계좌번호 사용중지 상태"),
    VIRTUAL_ACCOUNT_ACCOUNT_NUMBER_GENERATION_HISTORY_DOES_NOT_EXIST(HttpStatus.BAD_REQUEST, "4119", "가상계좌 채번내역 미존재 오류"),
    VIRTUAL_ACCOUNT_OVERPAYMENT_CHECK_REGISTER_FAILURE(HttpStatus.BAD_REQUEST, "4121", "가상계좌 과오납체크 등록 실패"),
    VIRTUAL_ACCOUNT_DEPOSIT_REQUEST_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "4122", "가상계좌 입금요청 금액 불일치 오류"),
    VIRTUAL_ACCOUNT_NUMBER_CANCELLED_REFUND_STATUS(HttpStatus.BAD_REQUEST, "4123", "가상계좌 채번취소(환불) 상태이므로 입금처리 실패"),
    VIRTUAL_ACCOUNT_DEPOSIT_HISTORY_PROCESSED(HttpStatus.BAD_REQUEST, "4124", "가상계좌 입금내역 기처리 완료"),
    VIRTUAL_ACCOUNT_DEPOSIT_EXPIRY_LIMIT_SETTING_ERROR(HttpStatus.BAD_REQUEST, "4125", "가상계좌 입금만료 제한 설정오류(기준정보)"),
    VIRTUAL_ACCOUNT_CP_ID_SETTING_ERROR(HttpStatus.BAD_REQUEST, "4126", "가상계좌 CPID 설정 오류"),
    VIRTUAL_ACCOUNT_CP_ID_NOT_SET_ERROR(HttpStatus.BAD_REQUEST, "4127", "가상계좌 CPID 미설정 오류"),
    AMOUNT_ERROR2(HttpStatus.BAD_REQUEST, "4141", "금액오류(1원 이하 이체불가)"),
    VIRTUAL_ACCOUNT_OVERPAYMENT_CHECK_NOT_USED_MERCHANT(HttpStatus.BAD_REQUEST, "4142", "가상계좌 과오납체크 미사용 가맹점"),
    VIRTUAL_ACCOUNT_OVERPAYMENT_CHECK_NOT_REGISTERED_ERROR(HttpStatus.BAD_REQUEST, "4143", "가상계좌 과오납체크 미등록 오류"),
    MODIFICATION_OF_DEPOSITOR_NAME_NOT_POSSIBLE_MID(HttpStatus.BAD_REQUEST, "4145", "예금주명 수정이 불가능한 MID"),CASH_RECEIPT_SUCCESS(HttpStatus.OK, "7001", "현금영수증 처리 성공"),
    CASH_RECEIPT_INVALID_TYPE(HttpStatus.BAD_REQUEST, "7002", "현금영수증 종류 오류"),
    CASH_RECEIPT_DUPLICATE_ISSUE(HttpStatus.BAD_REQUEST, "7003", "현금영수증 중복 발급"),
    CASH_RECEIPT_CANCEL_ERROR(HttpStatus.BAD_REQUEST, "7004", "현금영수증 취소 오류"),
    CASH_RECEIPT_VAT_ERROR(HttpStatus.BAD_REQUEST, "7005", "현금영수증 부가가치세 오류"),
    CASH_RECEIPT_MAX_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "7006", "현금영수증 최대 개수 초과"),
    CASH_RECEIPT_REQUEST_COUNT_INPUT_ERROR(HttpStatus.BAD_REQUEST, "7007", "현금영수증 요청 개수 입력 오류"),
    CASH_RECEIPT_UNREGISTERED_SUBMALL(HttpStatus.BAD_REQUEST, "7008", "현금영수증 서브몰 발행 미등록 업체"),
    CASH_RECEIPT_INVALID_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "7009", "현금영수증 처리 지불 수단이 아닙니다."),
    CASH_RECEIPT_VAN_RESPONSE_FAILURE(HttpStatus.BAD_REQUEST, "7010", "VAN 응답 실패"),
    CASH_RECEIPT_NOT_ISSUED_REQUEST(HttpStatus.BAD_REQUEST, "7011", "현금영수증 미발행 요청입니다."),
    CASH_RECEIPT_IDENTITY_NUMBER_ERROR(HttpStatus.BAD_REQUEST, "7012", "현금영수증 Identity 번호 오류"),
    CASH_RECEIPT_REQUEST_TYPE_ERROR(HttpStatus.BAD_REQUEST, "7013", "현금영수증 요청 구분 값 오류 (1:소득공제, 2:지출증빙)"),
    CASH_RECEIPT_MISSING_MERCHANT_TAX_NUMBER(HttpStatus.BAD_REQUEST, "7014", "현금영수증 서브몰 사업자번호로 발행 시 필수값 누락"),
    CASH_RECEIPT_MISSING_ORIGINAL_APPROVAL_INFO(HttpStatus.BAD_REQUEST, "7015", "현금영수증 취소 시 원승인번호 또는 원거래일자 누락"),
    CASH_RECEIPT_AMOUNT_ERROR(HttpStatus.BAD_REQUEST, "7041", "금액 오류 (0원 이하 발행 불가)"),
    CASH_RECEIPT_MAX_AMOUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "7042", "현금영수증 최대 금액 초과 오류"),

    // 다른 상태 코드와 설명

    MOBILE_PAYMENT_SUCCESS(HttpStatus.OK, "A000", "휴대폰 결제 처리 성공"),
    MOBILE_PAYMENT_FAILURE(HttpStatus.BAD_REQUEST, "A001", "휴대폰 결제 처리 실패"),
    MOBILE_PAYMENT_MISSING_TRANSACTION_KEY(HttpStatus.BAD_REQUEST, "A002", "필수 입력값(거래키) 누락"),
    MOBILE_PAYMENT_MISSING_MOBILE_CARRIER(HttpStatus.BAD_REQUEST, "A003", "필수 입력값(이통사 구분) 누락"),
    MOBILE_PAYMENT_MISSING_SMS_APPROVAL_NUMBER(HttpStatus.BAD_REQUEST, "A004", "필수 입력값(SMS 승인번호) 누락"),
    MOBILE_PAYMENT_MISSING_MERCHANT_TID(HttpStatus.BAD_REQUEST, "A005", "필수 입력값(업체 TID) 누락"),
    MOBILE_PAYMENT_MISSING_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "A006", "필수 입력값(휴대폰 번호) 누락"),
    MOBILE_PAYMENT_AMOUNT_ERROR(HttpStatus.BAD_REQUEST, "A041", "결제 금액 오류"),
    MOBILE_PAYMENT_INVALID_PAYMENT_ID(HttpStatus.BAD_REQUEST, "A564", "휴대폰 결제 ID 설정 오류"),
    MOBILE_PAYMENT_MISSING_PAYMENT_ID(HttpStatus.BAD_REQUEST, "A565", "휴대폰 결제 ID 미설정 오류"),
    MOBILE_PAYMENT_INVALID_PAYMENT_PROVIDER(HttpStatus.BAD_REQUEST, "A566", "휴대폰 결제사 설정 오류"),
    MOBILE_PAYMENT_INVALID_PRODUCT_CODE(HttpStatus.BAD_REQUEST, "A567", "상품 구분 코드 설정 오류"),
    MOBILE_PAYMENT_INVALID_SERVICE_CODE(HttpStatus.BAD_REQUEST, "A568", "서비스 구분 코드 설정 오류"),

    // 다른 상태 코드와 설명

    PAYMENT_SUCCESS(HttpStatus.OK, "0000", "결제 성공"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "2000", "DB 오류"),
    CINO_NOT_FOUND(HttpStatus.BAD_REQUEST, "2011", "CINO 미존재"),
    ORDER_NUMBER_MISSING(HttpStatus.BAD_REQUEST, "2012", "주문번호 없음"),
    MERCHANT_ORDER_NUMBER_TOO_LONG(HttpStatus.BAD_REQUEST, "2032", "가맹점 주문번호 길이 이상"),
    TRANSACTION_SUSPENDED(HttpStatus.BAD_REQUEST, "2151", "거래 정지 가맹점"),
    UNREGISTERED_MERCHANT(HttpStatus.BAD_REQUEST, "2152", "미등록 가맹점"),
    PARTNER_STATUS_NOT_CONFIRMED(HttpStatus.BAD_REQUEST, "2154", "제휴사 상태 미확인"),
    DUPLICATE_TRANSACTION_REQUEST(HttpStatus.BAD_REQUEST, "2156", "중복 등록된 거래 요청"),
    UNSUPPORTED_INPUT_METHOD(HttpStatus.BAD_REQUEST, "2157", "허용되지 않는 입력 방식"),
    DUPLICATE_INPUT_METHOD(HttpStatus.BAD_REQUEST, "2158", "중복 등록된 입력 방식"),
    BANK_OUTAGE(HttpStatus.BAD_REQUEST, "2159", "해당 은행 장애"),
    PREVIOUS_APPROVAL_EXISTS(HttpStatus.BAD_REQUEST, "2201", "기 승인 존재"),
    BILL_KEY_GENERATION_SUCCESS(HttpStatus.OK, "F100", "빌키가 정상적으로 생성되었습니다."),
    BILLING_INFO_DELETION_SUCCESS(HttpStatus.OK, "F101", "빌링 정보가 정상적으로 삭제되었습니다."),
    CERTIFICATE_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "F102", "인증서 검증 오류"),
    PKCS7_SIGNATURE_VALIDATION_FAILURE(HttpStatus.BAD_REQUEST, "F103", "PKCS7 전자서명 검증 실패"),
    AUTHENTICATION_INFO_ERROR(HttpStatus.BAD_REQUEST, "F104", "인증 정보 확인 중 오류가 발생하였습니다."),
    OCSP_AUTHENTICATION_ERROR(HttpStatus.BAD_REQUEST, "F105", "OCSP 인증 중 오류가 발생하였습니다."),
    DB_PROCESSING_ERROR_TRANS_REQ(HttpStatus.BAD_REQUEST, "F106", "DB 처리 중 오류가 발생하였습니다. (TB_TRANS_REQ)"),
    DB_PROCESSING_ERROR_TRANS(HttpStatus.BAD_REQUEST, "F107", "DB 처리 중 오류가 발생하였습니다. (TB_TRANS)"),
    DB_PROCESSING_ERROR_TRANS_HISTORY(HttpStatus.BAD_REQUEST, "F108", "DB 처리 중 오류가 발생하였습니다. (TB_TRANS_HISTORY)"),
    DB_PROCESSING_ERROR_BILL_MASTER(HttpStatus.BAD_REQUEST, "F109", "DB 처리 중 오류가 발생하였습니다. (TB_BILL_MASTER)"),
    BILL_KEY_ISSUANCE_ERROR(HttpStatus.BAD_REQUEST, "F110", "빌키 발급 처리 중 오류가 발생하였습니다."),
    PKCS7_SIGNATURE_MESSAGE_MISSING(HttpStatus.BAD_REQUEST, "F111", "PKCS7 전자서명 메시지가 존재하지 않습니다."),
    INVALID_CARD_NUMBER(HttpStatus.BAD_REQUEST, "F112", "유효하지 않은 카드 번호를 입력하셨습니다. (card_bin 없음)"),
    SELF_CREDIT_CARD_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "F113", "본인의 신용카드 확인 중 오류가 발생하였습니다."),
    INVALID_CERTIFICATE(HttpStatus.BAD_REQUEST, "F114", "인증서가 유효하지 않습니다."),
    BILL_KEY_ISSUANCE_NOT_ALLOWED_SUSPENDED(HttpStatus.BAD_REQUEST, "F115", "빌키 발급 불가 가맹점입니다. (중지)"),
    BILL_KEY_ISSUANCE_NOT_ALLOWED_TERMINATED(HttpStatus.BAD_REQUEST, "F116", "빌키 발급 불가 가맹점입니다. (해지)"),
    BILLING_NOT_USED_MERCHANT(HttpStatus.BAD_REQUEST, "F117", "빌링 미사용 가맹점입니다."),
    INVALID_CARD(HttpStatus.BAD_REQUEST, "F118", "해당 카드는 사용이 불가능합니다. 타사 카드를 이용해주세요."),
    BILLING_APPROVAL_SUCCESS(HttpStatus.OK, "F200", "빌링 요청 승인이 정상적으로 이루어졌습니다."),
    ALREADY_REGISTERED_CARD(HttpStatus.BAD_REQUEST, "F201", "이미 등록된 카드입니다. (빌키 발급 실패)"),
    ESCROW_DELIVERY_SUCCESS(HttpStatus.OK, "C000", "에스크로 배송 등록 성공"),
    NOT_ESCROW_MERCHANT(HttpStatus.BAD_REQUEST, "C002", "에스크로 가맹점 아님"),
    ESCROW_TRANSACTION_ONLY(HttpStatus.BAD_REQUEST, "C003", "에스크로 거래만 배송 등록 가능"),
    ESCROW_PAYMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "C004", "에스크로 결제 신청 내역 미존재"),
    ESCROW_DELIVERY_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "C005", "에스크로 배송 등록 불가 상태"),
    TRANSACTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "C006", "거래 내역이 존재하지 않음"),
    ESCROW_DELIVERY_CANCELLED(HttpStatus.BAD_REQUEST, "C007", "취소된 거래는 배송 등록 불가"),
    ESCROW_PURCHASE_DECISION_SUCCESS(HttpStatus.OK, "D000", "에스크로 구매 결정 성공"),
    NOT_ESCROW_MERCHANT_DECISION(HttpStatus.BAD_REQUEST, "D002", "에스크로 가맹점 아님"),
    ESCROW_TRANSACTION_ONLY_DECISION(HttpStatus.BAD_REQUEST, "D003", "에스크로 거래만 구매 결정 가능"),
    ESCROW_PAYMENT_NOT_FOUND_DECISION(HttpStatus.BAD_REQUEST, "D004", "에스크로 결제 신청 내역 미존재"),
    ESCROW_DECISION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "D005", "에스크로 구매 결정 불가 상태"),
    NO_CUSTOMER_UNIQUE_NUMBER(HttpStatus.BAD_REQUEST, "D007", "고객 고유 번호 미입력"),
    TRANSACTION_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "D008", "거래 요청 내역이 존재하지 않음"),
    CUSTOMER_UNIQUE_NUMBER_VALIDATION_FAILURE(HttpStatus.BAD_REQUEST, "D009", "고객 고유 번호 검증 실패"),
    ESCROW_DECISION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "D010", "구매 결정 이미 처리됨"),
    ESCROW_CANCELLED_TRANSACTION(HttpStatus.BAD_REQUEST, "D011", "취소된 거래는 구매 결정 불가"),
    ESCROW_PURCHASE_REJECTION_SUCCESS(HttpStatus.OK, "E000", "에스크로 구매 거절 성공"),
    NOT_ESCROW_MERCHANT_REJECTION(HttpStatus.BAD_REQUEST, "E002", "에스크로 가맹점 아님"),
    ESCROW_TRANSACTION_ONLY_REJECTION(HttpStatus.BAD_REQUEST, "E003", "에스크로 거래만 구매 거절 가능"),
    ESCROW_PAYMENT_NOT_FOUND_REJECTION(HttpStatus.BAD_REQUEST, "E004", "에스크로 결제 신청 내역 미존재"),
    ESCROW_REJECTION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "E005", "에스크로 구매 거절 불가 상태"),
    NO_CUSTOMER_UNIQUE_NUMBER_REJECTION(HttpStatus.BAD_REQUEST, "E007", "고객 고유 번호 미입력"),
    TRANSACTION_REQUEST_NOT_FOUND_REJECTION(HttpStatus.BAD_REQUEST, "E008", "거래 요청 내역이 존재하지 않음"),
    CUSTOMER_UNIQUE_NUMBER_VALIDATION_FAILURE_REJECTION(HttpStatus.BAD_REQUEST, "E009", "고객 고유 번호 검증 실패"),
    ESCROW_REJECTION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "E010", "구매 거절 이미 처리됨"),
    ESCROW_CANCELLED_TRANSACTION_REJECTION(HttpStatus.BAD_REQUEST, "E011", "취소된 거래는 구매 거절 불가"),
    CANCELLATION_SUCCESS(HttpStatus.OK, "2001", "취소 성공"),
    REFUND_SUCCESS(HttpStatus.OK, "2211", "환불 성공 (취소 성공 처리와 함께)"),
    CANCELLATION_FAILURE(HttpStatus.BAD_REQUEST, "2003", "취소 실패"),
    CANCELLATION_REQUEST_AMOUNT_TOO_LOW(HttpStatus.BAD_REQUEST, "2010", "취소 요청 금액 0원 이하"),
    CANCELLATION_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "2011", "취소 금액 불일치"),
    NO_MATCHING_TRANSACTION(HttpStatus.BAD_REQUEST, "2012", "취소 해당 거래 없음"),
    COMPLETED_CANCELLATION(HttpStatus.BAD_REQUEST, "2013", "취소 완료 거래"),
    CANCELLATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "2014", "취소 불가능 거래"),
    PREVIOUS_CANCELLATION_REQUEST(HttpStatus.BAD_REQUEST, "2015", "기 취소 요청"),
    CANCELLATION_TIME_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "2016", "취소 기한 초과"),
    CANCELLATION_NOT_ALLOWED_MERCHANT(HttpStatus.BAD_REQUEST, "2017", "취소 불가 회원사"),
    CANCELLATION_NOT_ALLOWED_AFTER_SETTLEMENT(HttpStatus.BAD_REQUEST, "2018", "신용카드 매입 후 취소 불가능 가맹점"),
    CANCELLATION_NOT_ALLOWED_OTHER_MERCHANT(HttpStatus.BAD_REQUEST, "2019", "타 회원사 거래 취소 불가"),
    CANCELLATION_PRE_SUCCESS(HttpStatus.OK, "2021", "매입전취소 성공"),
    CANCELLATION_POST_SUCCESS(HttpStatus.OK, "2022", "매입후취소 성공"),
    CANCELLATION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "2023", "취소 한도 초과"),
    CANCELLATION_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "2024", "취소패스워드 불일치"),
    CANCELLATION_PASSWORD_NOT_ENTERED(HttpStatus.BAD_REQUEST, "2025", "취소패스워드 미 입력"),
    CANCELLATION_AMOUNT_GREATER_THAN_DEPOSIT(HttpStatus.BAD_REQUEST, "2026", "입금액보다 취소금액이 큽니다."),
    ESCROW_CANCELATION_VALIDATION(HttpStatus.BAD_REQUEST, "2027", "에스크로 거래는 구매 또는 구매거절 시 취소가능."),
    PARTIAL_CANCELLATION_NOT_ALLOWED_MERCHANT(HttpStatus.BAD_REQUEST, "2028", "부분취소 불가능 가맹점."),
    PARTIAL_CANCELLATION_NOT_ALLOWED_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "2029", "부분취소 불가능 결제수단."),
    PARTIAL_CANCELLATION_NOT_ALLOWED_PAYMENT_METHOD2(HttpStatus.BAD_REQUEST, "2030", "해당결제수단 부분취소 불가."),
    FULL_AMOUNT_CANCELLATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "2031", "전체금액취소 불가."),
    CANCELLATION_AMOUNT_GREATER_THAN_ALLOWED(HttpStatus.BAD_REQUEST, "2032", "취소금액이 취소가능금액보다 큼."),
    PARTIAL_CANCELLATION_NOT_ALLOWED_AMOUNT(HttpStatus.BAD_REQUEST, "2033", "부분취소 불가능금액. 전체취소 이용바람."),
    A101(HttpStatus.BAD_REQUEST, "A101", "SIGN DATA 검증에 실패하였습니다."),
    A102(HttpStatus.BAD_REQUEST, "A102", "타입이 맞지 않는 파라미터명 명시 + 은(는) 알 수 없는 TYPE 입니다."),
    A106(HttpStatus.BAD_REQUEST, "A106", "날짜 형식이 올바르지 않습니다."),
    A107(HttpStatus.BAD_REQUEST, "A107", "입금예정일이 지났습니다."),
    A108(HttpStatus.BAD_REQUEST, "A108", "입금예정일을 오늘 이전 날짜로 설정할 수 없습니다."),
    A109(HttpStatus.BAD_REQUEST, "A109", "요청한 MID의 설정 정보가 없습니다."),
    A110(HttpStatus.INTERNAL_SERVER_ERROR, "A110", "외부 연동 결과 실패에 대한 코드(외부 에러 메세지 그대로 전달)"),
    A111(HttpStatus.BAD_REQUEST, "A111", "다날 휴대폰 통신 오류입니다."),
    A112(HttpStatus.FORBIDDEN, "A112", "비정상적인 경로로 접속되었습니다."),
    A113(HttpStatus.BAD_REQUEST, "A113", "결제 금액이 최소 금액보다 적습니다."),
    A114(HttpStatus.NOT_FOUND, "A114", "상점 MID가 유효하지 않습니다."),
    A115(HttpStatus.NOT_FOUND, "A115", "TID가 유효하지 않습니다."),
    A116(HttpStatus.BAD_REQUEST, "A116", "요청 금액이 올바르지 않습니다."),
    A117(HttpStatus.BAD_REQUEST, "A117", "필수 입력 항목이 누락되었습니다."),
    A118(HttpStatus.NOT_FOUND, "A118", "조회 결과 데이터 없음"),
    A119(HttpStatus.NOT_FOUND, "A119", "일반 무이자 이벤트 조회 결과 없음"),
    A120(HttpStatus.NOT_FOUND, "A120", "부분 무이자 이벤트 조회 결과 없음"),
    A121(HttpStatus.BAD_REQUEST, "A121", "정의되지 않은 카드 코드입니다."),
    A122(HttpStatus.BAD_REQUEST, "A122", "타 상점 거래 처리 불가 (MID 불일치)"),
    A123(HttpStatus.BAD_REQUEST, "A123", "거래 금액 불일치 (인증된 금액과 승인 요청 금액 불일치)"),
    A124(HttpStatus.NOT_FOUND, "A124", "해당 BID가 존재하지 않습니다."),
    A125(HttpStatus.NOT_FOUND, "A125", "BID가 유효하지 않습니다."),
    A126(HttpStatus.NOT_FOUND, "A126", "이미 삭제된 빌키이거나 존재하지 않은 빌키입니다."),
    A127(HttpStatus.BAD_REQUEST, "A127", "주문번호 중복 오류"),
    A128(HttpStatus.BAD_REQUEST, "A128", "키인 가맹점이 아닙니다."),
    A144(HttpStatus.INTERNAL_SERVER_ERROR, "A144", "가맹점 결과 통보에 실패하였습니다."),
    A145(HttpStatus.BAD_REQUEST, "A145", "외화 결제는 결제 수단을 지정하여 이용 가능합니다."),
    A146(HttpStatus.BAD_REQUEST, "A146", "외화 결제 불가능한 결제 수단입니다."),
    A147(HttpStatus.BAD_REQUEST, "A147", "필드 길이가 초과되었습니다."),
    A148(HttpStatus.BAD_REQUEST, "A148", "빌링 승인은 별도 API를 이용하십시오."),
    A149(HttpStatus.BAD_REQUEST, "A149", "빌링 승인만 가능한 API입니다. (일반 결제는 별도 API 이용)"),
    A150(HttpStatus.BAD_REQUEST, "A150", "ARS 이용 가맹점이 아닙니다."),
    A201(HttpStatus.BAD_REQUEST, "A201", "PID 생성이 누락되었습니다."),
    A202(HttpStatus.BAD_REQUEST, "A202", "결과 코드 생성이 누락되었습니다."),
    A203(HttpStatus.BAD_REQUEST, "A203", "결과 메시지 생성이 누락되었습니다."),
    A204(HttpStatus.BAD_REQUEST, "A204", "전문 복호화 오류가 발생하였습니다."),
    A205(HttpStatus.BAD_REQUEST, "A205", "전문 암호화 오류가 발생하였습니다."),
    A210(HttpStatus.BAD_REQUEST, "A210", "인증 요청 내역이 존재하지 않습니다."),
    A211(HttpStatus.BAD_REQUEST, "A211", "해쉬값 검증에 실패하였습니다."),
    A212(HttpStatus.BAD_REQUEST, "A212", "잘못된 데이터 형식입니다."),
    A213(HttpStatus.INTERNAL_SERVER_ERROR, "A213", "API 초기화 오류."),
    A214(HttpStatus.NOT_FOUND, "A214", "해당하는 카드 거래가 없습니다."),
    A215(HttpStatus.NOT_FOUND, "A215", "해당하는 핸드폰 거래가 없습니다."),
    A216(HttpStatus.NOT_FOUND, "A216", "해당하는 계좌 이체 거래가 없습니다."),
    A217(HttpStatus.NOT_FOUND, "A217", "해당하는 가상계좌 거래가 없습니다."),
    A218(HttpStatus.NOT_FOUND, "A218", "해당하는 전자상품권 거래가 없습니다."),
    A219(HttpStatus.INTERNAL_SERVER_ERROR, "A219", "환율 정보 설정 오류입니다."),
    A220(HttpStatus.NOT_FOUND, "A220", "설정된 환율 정보가 없습니다."),
    A221(HttpStatus.NOT_FOUND, "A221", "노티 수신 정보 없음."),
    A222(HttpStatus.BAD_REQUEST, "A222", "카드 코드가 일치하지 않습니다."),
    A223(HttpStatus.NOT_FOUND, "A223", "billKey 정보를 찾을 수 없습니다."),
    A224(HttpStatus.FORBIDDEN, "A224", "허용되지 않은 IP입니다."),
    A225(HttpStatus.BAD_REQUEST, "A225", "TID 중복 오류"),
    A226(HttpStatus.INTERNAL_SERVER_ERROR, "A226", "전문 통신 과정에서 오류가 발생하였습니다."),
    A227(HttpStatus.BAD_REQUEST, "A227", "필드명 중복 발생"),
    A241(HttpStatus.BAD_REQUEST, "A241", "VISA3D 인증을 이용할 수 없는 카드입니다."),
    A242(HttpStatus.BAD_REQUEST, "A242", "정의되지 않은 지불 수단입니다."),
    A243(HttpStatus.NOT_FOUND, "A243", "주문 정보가 존재하지 않습니다."),
    A244(HttpStatus.INTERNAL_SERVER_ERROR, "A244", "주문 내역 갱신에 실패하였습니다."),
    A245(HttpStatus.BAD_REQUEST, "A245", "인증 시간이 초과되었습니다."),
    A246(HttpStatus.TOO_MANY_REQUESTS, "A246", "비정상 과다 접속으로 인한 오류입니다."),
    A247(HttpStatus.BAD_REQUEST, "A247", "정의되지 않은 통화 코드입니다."),
    A248(HttpStatus.BAD_REQUEST, "A248", "사용할 수 없는 화폐 단위입니다. (계약 정보 확인 필요)"),
    A249(HttpStatus.INTERNAL_SERVER_ERROR, "A249", "주문 정보 저장 실패 (ORDER_DATA 최대 길이 초과)"),
    A250(HttpStatus.BAD_REQUEST, "A250", "휴대폰 번호 변조 오류 (요청 번호와 인증 완료 번호 상이)"),
    A251(HttpStatus.NOT_FOUND, "A251", "거래 내역이 존재하지 않습니다."),
    A252(HttpStatus.INTERNAL_SERVER_ERROR, "A252", "Signature 생성에 실패하였습니다."),
    A253(HttpStatus.INTERNAL_SERVER_ERROR, "A253", "빌키 (BID) 생성에 실패하였습니다."),
    A254(HttpStatus.BAD_REQUEST, "A254", "제휴사 응답 전문이 유효하지 않습니다."),
    A255(HttpStatus.FORBIDDEN, "A255", "타 상점 빌키 (BID) 삭제 불가."),
    A256(HttpStatus.BAD_REQUEST, "A256", "ARS 인증번호 생성 실패. 잠시 후 재시도 바랍니다."),
    A257(HttpStatus.INTERNAL_SERVER_ERROR, "A257", "주문 정보 저장에 실패하였습니다."),
    A258(HttpStatus.INTERNAL_SERVER_ERROR, "A258", "SMS 발송에 실패하였습니다."),
    A299(HttpStatus.INTERNAL_SERVER_ERROR, "A299", "API 지연 처리 발생."),
    A300(HttpStatus.INTERNAL_SERVER_ERROR, "A300", "기준 정보 조회 오류."),
    A301(HttpStatus.NOT_FOUND, "A301", "가맹점 키 조회 오류입니다."),
    A302(HttpStatus.INTERNAL_SERVER_ERROR, "A302", "기준 정보 상 필수 설정 정보가 없습니다."),
    A303(HttpStatus.INTERNAL_SERVER_ERROR, "A303", "기준 정보 CPID 설정 정보가 없습니다."),
    A304(HttpStatus.INTERNAL_SERVER_ERROR, "A304", "DB 테이블 INSERT 오류 발생."),
    A305(HttpStatus.INTERNAL_SERVER_ERROR, "A305", "가맹점 번호 기준 정보 미 설정 오류."),
    A306(HttpStatus.INTERNAL_SERVER_ERROR, "A306", "DB 테이블 UPDATE 실패."),
    A307(HttpStatus.BAD_REQUEST, "A307", "기준 정보 조회 결과 2행 이상 오류"),
    A400(HttpStatus.INTERNAL_SERVER_ERROR, "A400", "대외 기관 전문 생성 실패하였습니다."),
    A401(HttpStatus.INTERNAL_SERVER_ERROR, "A401", "대외 계 전문 통신 과정에서 오류가 발생하였습니다."),
    A402(HttpStatus.INTERNAL_SERVER_ERROR, "A402", "DB 트랜잭션 처리에 실패하였습니다."),
    A403(HttpStatus.BAD_REQUEST, "A403", "대외 기관 응답 전문이 올바르지 않습니다."),
    S999(HttpStatus.INTERNAL_SERVER_ERROR, "S999", "기타 오류가 발생하였습니다."),
    S001(HttpStatus.BAD_REQUEST, "S001", "요청 템플릿이 존재하지 않습니다."),
    S002(HttpStatus.BAD_REQUEST, "S002", "응답 템플릿이 존재하지 않습니다."),
    T001(HttpStatus.INTERNAL_SERVER_ERROR, "T001", "수신 메시지 인코딩 중 예외가 발생하였습니다."),
    T002(HttpStatus.BAD_REQUEST, "T002", "비정상적인 수신 전문입니다."),
    T003(HttpStatus.INTERNAL_SERVER_ERROR, "T003", "수신 데이터 파싱 중 예외가 발생하였습니다."),
    T004(HttpStatus.BAD_REQUEST, "T004", "요청 전문의 헤더 부 생성 중 오류가 발생하였습니다."),
    T005(HttpStatus.BAD_REQUEST, "T005", "요청 전문의 바디 부 생성 중 오류가 발생하였습니다."),
    X001(HttpStatus.BAD_REQUEST, "X001", "서버 도메인명이 잘못 설정되었습니다."),
    X002(HttpStatus.INTERNAL_SERVER_ERROR, "X002", "서버로 소켓 연결 중 오류가 발생하였습니다."),
    X003(HttpStatus.INTERNAL_SERVER_ERROR, "X003", "전문 수신 중 오류가 발생하였습니다."),
    X004(HttpStatus.INTERNAL_SERVER_ERROR, "X004", "전문 송신 중 오류가 발생하였습니다."),
    V005(HttpStatus.BAD_REQUEST, "V005", "지원하지 않는 지불 수단입니다."),
    V101(HttpStatus.BAD_REQUEST, "V101", "암호화 플래그 미설정 오류입니다."),
    V102(HttpStatus.BAD_REQUEST, "V102", "서비스 모드를 설정하지 않았습니다."),
    V103(HttpStatus.BAD_REQUEST, "V103", "지불 수단을 설정하지 않았습니다."),
    V104(HttpStatus.BAD_REQUEST, "V104", "상품 개수 미설정 오류입니다."),
    V201(HttpStatus.BAD_REQUEST, "V201", "상점 ID 미설정 오류입니다."),
    V202(HttpStatus.BAD_REQUEST, "V202", "LicenseKey 미설정 오류입니다."),
    V203(HttpStatus.BAD_REQUEST, "V203", "통화 구분 미설정 오류입니다."),
    V204(HttpStatus.BAD_REQUEST, "V204", "MID 미설정 오류입니다."),
    V205(HttpStatus.BAD_REQUEST, "V205", "MallIP 미설정 오류입니다."),
    V301(HttpStatus.BAD_REQUEST, "V301", "구매자 이름 미설정 오류입니다."),
    V302(HttpStatus.BAD_REQUEST, "V302", "구매자 인증번호 미설정 오류입니다."),
    V303(HttpStatus.BAD_REQUEST, "V303", "구매자 연락처 미설정 오류입니다."),
    V304(HttpStatus.BAD_REQUEST, "V304", "구매자 메일 주소 미설정 오류입니다."),
    V401(HttpStatus.BAD_REQUEST, "V401", "상품명 미설정 오류입니다."),
    V402(HttpStatus.BAD_REQUEST, "V402", "상품 금액 미설정 오류입니다."),
    V501(HttpStatus.BAD_REQUEST, "V501", "카드 형태 미설정 오류입니다."),
    V502(HttpStatus.BAD_REQUEST, "V502", "카드 구분 미설정 오류입니다."),
    V503(HttpStatus.BAD_REQUEST, "V503", "카드 코드 미설정 오류입니다."),
    V504(HttpStatus.BAD_REQUEST, "V504", "카드 번호 미설정 오류입니다."),
    V505(HttpStatus.BAD_REQUEST, "V505", "카드 무이자 여부 미설정 오류입니다."),
    V506(HttpStatus.BAD_REQUEST, "V506", "카드 인증 구분 미설정 오류입니다."),
    V507(HttpStatus.BAD_REQUEST, "V507", "카드 형태 설정 오류입니다."),
    V508(HttpStatus.BAD_REQUEST, "V508", "카드 형태 허용하지 않는 값을 설정하였습니다."),
    V509(HttpStatus.BAD_REQUEST, "V509", "카드 구분 허용하지 않는 값을 설정하였습니다."),
    V510(HttpStatus.BAD_REQUEST, "V510", "유효 기간 미설정 오류입니다."),
    V511(HttpStatus.BAD_REQUEST, "V511", "유효 기간 허용하지 않는 값을 설정하였습니다."),
    V512(HttpStatus.BAD_REQUEST, "V512", "유효 기간의 월 형태가 잘못 설정되었습니다."),
    V513(HttpStatus.BAD_REQUEST, "V513", "카드 비밀번호 미입력 오류입니다."),
    V601(HttpStatus.BAD_REQUEST, "V601", "은행 코드 미설정 오류입니다."),
    V602(HttpStatus.BAD_REQUEST, "V602", "금융 결제원 암호화 데이터 미설정 오류입니다."),
    V701(HttpStatus.BAD_REQUEST, "V701", "가상계좌 입금 만료일 미설정 오류입니다."),
    VA01(HttpStatus.BAD_REQUEST, "VA01", "거래 KEY 미설정 오류입니다."),
    VA02(HttpStatus.BAD_REQUEST, "VA02", "이통사 구분 미설정 오류입니다."),
    VA03(HttpStatus.BAD_REQUEST, "VA03", "SMS 승인번호 미설정 오류입니다."),
    VA04(HttpStatus.BAD_REQUEST, "VA04", "업체 TID 미설정 오류입니다."),
    VA05(HttpStatus.BAD_REQUEST, "VA05", "휴대폰 번호 미설정 오류입니다."),
    VA09(HttpStatus.BAD_REQUEST, "VA09", "고객 고유번호(주민번호, 사업자번호) 미설정 오류입니다."),
    VA10(HttpStatus.BAD_REQUEST, "VA10", "ENCODE 업체 TID 미설정 오류입니다."),
    VB02(HttpStatus.BAD_REQUEST, "VB02", "이통사 구분 미설정 오류입니다."),
    VB05(HttpStatus.BAD_REQUEST, "VB05", "휴대폰 번호 미설정 오류입니다."),
    VB09(HttpStatus.BAD_REQUEST, "VB09", "고객 고유번호(주민번호, 사업자번호) 미설정 오류입니다."),
    VB10(HttpStatus.BAD_REQUEST, "VB10", "고객 IP 미설정 오류입니다."),
    V801(HttpStatus.BAD_REQUEST, "V801", "취소 금액 미설정 오류입니다."),
    V802(HttpStatus.BAD_REQUEST, "V802", "취소 사유 미설정 오류입니다."),
    V803(HttpStatus.BAD_REQUEST, "V803", "취소 패스워드 미설정 오류입니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder().message(message).code(code).isSuccess(false).build();
    }

    @Override
    public String getExplainError() throws NoSuchFieldException {
        Field field = this.getClass().getField(this.name());
        ExplainError annotation = field.getAnnotation(ExplainError.class);
        return Objects.nonNull(annotation) ? annotation.value() : this.getMessage();
    }

    @Override
    public ErrorReason getErrorReasonHttpStatus(){
        return ErrorReason.builder().message(message).code(code).isSuccess(false).httpStatus(httpStatus).build();
    }
}
