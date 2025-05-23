package id.ac.ui.cs.advprog.wallet.repository;

import id.ac.ui.cs.advprog.wallet.model.transaction.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByWalletUserId(UUID userId);
    List<TransactionEntity> findByType(String type);
    List<TransactionEntity> findByTypeAndCampaignId(String type, UUID campaignId);

    @Query("SELECT SUM(t.amount) FROM TransactionEntity t WHERE t.type = :type AND t.campaignId = :campaignId")
    BigDecimal sumAmountByTypeAndCampaignId(@Param("type") String type, @Param("campaignId") UUID campaignId);
}