import javax.servlet.annotation.WebFilter;
import com.googlecode.objectify.ObjectifyFilter;

/** Helper class for filtering data in a database. */
@WebFilter(urlPatterns = {"/*"})
public class ObjectifyWebFilter extends ObjectifyFilter {}