
public class TokenProvider {
    private String secretEncoded;

    protected void init(String secret) {
        secretEncoded = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public boolean isAvailable(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretEncoded).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
        } catch (Exception ex) {
            eventLogger.debug(UserServiceNotableEvents.W_AUTH_FAILURE, ex.getMessage());
            return false;
        }

        return true;
    }

    public String getRole(String token) {
        return (String) Jwts.parser()
                .setSigningKey(secretEncoded)
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }
}
