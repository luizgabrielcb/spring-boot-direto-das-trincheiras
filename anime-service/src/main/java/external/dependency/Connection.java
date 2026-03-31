package external.dependency;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class Connection {
    private final String host;
    private final String username;
    private final String password;
}
