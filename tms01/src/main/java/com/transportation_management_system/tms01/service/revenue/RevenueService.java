package com.transportation_management_system.tms01.service.revenue;

import com.transportation_management_system.tms01.dto.revenue.*;

import java.util.List;

public interface RevenueService {

    RevenueSummaryResponse getRevenueSummary();

    RevenueFinalResponse generateAndCreateRevenueFinal(RevenueFinalRequest request);

    RevenueFinalResponse updateRevenueFinal(Long id, RevenueFinalRequest request);

    RevenueFinalResponse getRevenueFinalById(Long id);

    List<RevenueFinalResponse> getAllRevenueFinals();

    void deleteRevenueFinal(Long id);
}
