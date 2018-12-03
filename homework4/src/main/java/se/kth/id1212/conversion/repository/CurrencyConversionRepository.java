package se.kth.id1212.conversion.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.kth.id1212.conversion.domain.CurrencyConversion;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversion, Long> {

}
