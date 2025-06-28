package org.example.repository;

import java.util.List;
import org.example.entity.HttpRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpRequestLogRepository extends JpaRepository<HttpRequestLog, Long> {
  List<HttpRequestLog> findByRemoteIp(String remoteIp);
}
