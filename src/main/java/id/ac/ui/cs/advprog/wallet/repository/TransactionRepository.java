package id.ac.ui.cs.advprog.wallet.repository;

import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByWalletUserId(Long userId);
}