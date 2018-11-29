package se.kth.id1212.currency.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>This is the bank application class, which defines tasks that can be
 * performed by the domain layer.</p>
 *
 * <p>Transaction demarcation is defined by methods in this class, a
 * transaction starts when a method is called from the presentation layer, and ends (commit or rollback) when that
 * method returns.</p>
 */
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
@Service
public class CurrencyService {


}