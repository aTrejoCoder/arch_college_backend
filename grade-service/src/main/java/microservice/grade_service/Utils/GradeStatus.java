package microservice.grade_service.Utils;

public enum GradeStatus {
    NORMAL_PENDING,
    DID_NOT_PRESENT_PENDING,
    ACCREDITED_PENDING,
    NORMAL,
    DID_NOT_PRESENT,
    ACCREDITED;

    public boolean isPendingStatus() {
        return this == NORMAL_PENDING || this == DID_NOT_PRESENT_PENDING || this == ACCREDITED_PENDING;

    }
}