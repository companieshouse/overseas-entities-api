package uk.gov.companieshouse.overseasentitiesapi.mocks;

import uk.gov.companieshouse.overseasentitiesapi.model.dto.PaymentDto;

public class PaymentMock {
    public static PaymentDto getPaymentDto() {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentResource("http://api.chs.local:4001/transactions/000000-000000-000000/payment");
        paymentDto.setPaymentState("00000000-0000-0000-0000-000000000000");
        paymentDto.setPaymentRedirectUri("http://chs.local/register-an-overseas-entity/transaction/000000-000000-000000/submission/000000000000000000000000/payment");
        paymentDto.setPaymentReference("OverseasEntitiesReference_000000-000000-000000");
        return paymentDto;
    }
}
