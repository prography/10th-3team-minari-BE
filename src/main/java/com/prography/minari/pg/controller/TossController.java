package com.prography.minari.pg.controller;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.pg.dto.TossPaymentCancel.TossPaymentCancelReqDto;
import com.prography.minari.pg.dto.TossPaymentConfirm.TossPaymentConfirmReqDto;
import com.prography.minari.pg.service.TossService;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TossController {

    private final TossService tossService;

    @PostMapping("/toss/payments/prepare")
    public ResponseEntity<CommonResponse> preparePayment(
            @RequestParam("orderId") String orderId,
            @RequestParam("amout") BigDecimal amount
    ) {
        tossService.prepare(orderId, amount);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/toss/payments/confirm")
    public ResponseEntity<CommonResponse> confirmPayment(
            @AuthenticationPrincipal User user,
            @RequestParam("paymentKey") String paymentKey,
            @RequestParam("orderId") String orderId,
            @RequestParam("amount") BigDecimal amount) {

        tossService.confirm(paymentKey, orderId, amount, user);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @GetMapping("/toss/payments/{paymentKey}")
    public ResponseEntity<CommonResponse<Object>> findPayment(@PathVariable("paymentKey") String paymentKey) {
        tossService.getPaymentByPaymentKey(paymentKey);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @GetMapping("/toss/payments/orders/{orderId}")
    public ResponseEntity<CommonResponse<Object>> findOrder(@PathVariable("orderId") String orderId) {
        tossService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/toss/payments/{paymentKey}/cancel")
    public ResponseEntity<CommonResponse<Object>> cancelPayment(@PathVariable("paymentKey") String paymentKey, @RequestBody TossPaymentCancelReqDto reqDto) {
        tossService.cancelPayment(paymentKey, reqDto);
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/toss/virtual-accounts")
    public ResponseEntity<CommonResponse<Object>> virtualAccounts() {
        return ResponseEntity.ok(CommonResponse.ok());
    }

    @PostMapping("/toss/billing/authorizations/issue")
    public ResponseEntity<CommonResponse<Object>> issueBillingAuthorization() {
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
