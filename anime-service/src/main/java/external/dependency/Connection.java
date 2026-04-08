package external.dependency;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Data
public class Connection {
    private final String host;
    private final String username;
    private final String password;
}
