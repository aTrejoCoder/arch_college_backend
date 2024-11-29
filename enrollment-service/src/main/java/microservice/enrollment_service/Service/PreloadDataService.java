package microservice.enrollment_service.Service;

public interface PreloadDataService<T> {
    void clear();
    void preload(String processId);
    void startPreload(String processId);
    String getPreloadStatus(String processId);
}
