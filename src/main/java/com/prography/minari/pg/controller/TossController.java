package com.prography.minari.pg.controller;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.pg.dto.TossPaymentCancel.TossPaymentCancelReqDto;
import com.prography.minari.pg.dto.TossPaymentConfirm.TossPaymentConfirmReqDto;
import com.prography.minari.pg.dto.TossPaymentPrepare.TossPaymentPrepareReqDto;
import com.prography.minari.pg.dto.common.PaymentResponse;
import com.prography.minari.pg.dto.common.TossPayment;
import com.prography.minari.pg.service.TossService;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TossController {

    private final TossService tossService;

    @PostMapping("/toss/payments/prepare")
    public ResponseEntity<CommonResponse> preparePayment(
            @RequestBody @Validated TossPaymentPrepareReqDto reqDto
    ) {
        tossService.prepare(reqDto);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/toss/payments/confirm")
    public ResponseEntity<CommonResponse> confirmPayment(
            @AuthenticationPrincipal User user,
            @RequestBody @Validated TossPaymentConfirmReqDto reqDto) {

        TossPayment payment = tossService.confirm(reqDto, user);
        return ResponseEntity.ok(CommonResponse.success(payment));
    }

    @GetMapping("/toss/payments/{paymentKey}")
    public ResponseEntity<CommonResponse> findPayment(@PathVariable("paymentKey") String paymentKey) {
        PaymentResponse paymentResponse = tossService.getPaymentByPaymentKey(paymentKey);
        return ResponseEntity.ok(CommonResponse.success(paymentResponse));
    }

    @GetMapping("/toss/payments/orders/{orderId}")
    public ResponseEntity<CommonResponse> findOrder(@PathVariable("orderId") String orderId) {
        tossService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/toss/payments/{paymentKey}/cancel")
    public ResponseEntity<CommonResponse> cancelPayment(@PathVariable("paymentKey") String paymentKey, @RequestBody TossPaymentCancelReqDto reqDto) {
        tossService.cancelPayment(paymentKey, reqDto);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/toss/virtual-accounts")
    public ResponseEntity<CommonResponse> virtualAccounts() {
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/toss/billing/authorizations/issue")
    public ResponseEntity<CommonResponse> issueBillingAuthorization() {
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @GetMapping("/toss/transactions")
    public ResponseEntity<CommonResponse<Object>> findTransaction(
            @RequestParam("startDate")ZonedDateTime startDate,
            @RequestParam("endDate")ZonedDateTime endDate,
            @RequestParam("startingAfter") String startingAfter,
            @RequestParam("limit") int limit) {
        tossService.getTransactionList(startDate, endDate, startingAfter, limit);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @GetMapping("/toss/settlements")
    public ResponseEntity<CommonResponse<Object>> findSettlement() {
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/toss/cash-receipts")
    public ResponseEntity<CommonResponse<Object>> issueCashReceipt() {
        return ResponseEntity.ok(CommonResponse.ok());
    }


    @PostMapping("/toss/cash-receipts/{receiptKey}/cancel")
    public ResponseEntity<CommonResponse<Object>> cancelCashReceipt() {
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @GetMapping("/toss/cash-receipts")
    public ResponseEntity<CommonResponse<Object>> findCashReceipt() {
        return ResponseEntity.ok(CommonResponse.ok());
    }

}
