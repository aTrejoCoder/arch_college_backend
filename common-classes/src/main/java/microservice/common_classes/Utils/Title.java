package microservice.common_classes.Utils;

import lombok.Getter;

@Getter
public enum Title {
    BACHELOR("B."),
    ARCHITECT("ARCH."),
    MASTER_OF_ARCHITECTURE("M.ARCH."),
    ENGINEER("ENG."),
    DOCTORATE("DR");

    private final String initials;

    Title(String initials) {
        this.initials = initials;
    }
}

