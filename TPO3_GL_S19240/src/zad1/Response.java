

package zad1;

public class Response {
    private int statusCode;
    private String body;

    public Response(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int Status() {
        return this.statusCode;
    }

    public String Body() {
        return this.body;
    }
}
