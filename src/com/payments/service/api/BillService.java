package com.payments.service.api;

import com.payments.model.Bill;

import java.util.List;

public interface BillService extends Service <Bill>{

    List<Bill> getAll(int startPosition, String orderBy);

    Bill getBillByNumber (String number);

}
