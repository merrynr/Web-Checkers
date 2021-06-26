package com.webcheckers.ui;

import com.webcheckers.application.GameCenter;
import com.webcheckers.application.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("ui-tier")
/**
 * Test Class for {@link GetSignInRoute} objects
 *
 * @author Merry Ren
 */
public class GetSignInRouteTest {
    private GetSignInRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    private PlayerLobby playerLobby;
    private GameCenter gameCenter;

    /**
     * Setup mock objects
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        response = mock(Response.class);

        playerLobby = mock(PlayerLobby.class);
        gameCenter = new GameCenter(playerLobby);

        CuT = new GetSignInRoute(gameCenter, engine);
    }

    /**
     * Test PostSignInRoute with an already existing but not logged in username
     */
    @Test
    public void signInExistingUserGoodTest() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        try {
            CuT.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute(GetSignInRoute.TITLE_ATTR, GetSignInRoute.TITLE);
            testHelper.assertViewModelAttribute(GetSignInRoute.SIGNIN_MSG_ATTR, GetSignInRoute.SIGNIN_MESSAGE);

            testHelper.assertViewName(GetSignInRoute.VIEW_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
