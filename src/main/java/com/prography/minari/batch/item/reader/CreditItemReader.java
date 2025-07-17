package com.prography.minari.batch.item.reader;

import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.CreditStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreditItemReader {

    @Bean
    public JdbcPagingItemReader<Credit> readExpiredCredit(DataSource dataSource) throws Exception {
        JdbcPagingItemReader<Credit> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setPageSize(100);

        // Query provider 설정
        PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM credit");
        queryProvider.setWhereClause("expired_date_time::date = CURRENT_DATE");
        queryProvider.setSortKeys(Map.of("id", Order.ASCENDING)); // 정렬 필수

        reader.setQueryProvider(queryProvider);

        // RowMapper 설정
        reader.setRowMapper((rs, rowNum) -> Credit.builder()
                .id(rs.getLong("id"))
                .expiredDateTime(rs.getTimestamp("expired_date_time").toLocalDateTime())
                .userId(rs.getLong("user_id"))
                .paymentId(rs.getLong("payment_id"))
                .amount(rs.getLong("amount"))
                .productId(rs.getLong("product_id"))
                .status(CreditStatus.valueOf(rs.getString("status")))
                .build());

        reader.afterPropertiesSet();

        return reader;
    }
}
