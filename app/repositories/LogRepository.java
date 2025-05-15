package repositories;

import exceptions.DBInteractionException;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Request;
import play.db.ebean.EbeanConfig;

import com.google.inject.Inject;
import modules.DatabaseExecutionContext;

import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import play.db.ebean.EbeanDynamicEvolutions;
import play.libs.Json;
import play.mvc.Http;

public class LogRepository
{
    private final EbeanServer db;
    private final EbeanDynamicEvolutions ebeanDynamicEvolutions;
    private final DatabaseExecutionContext databaseExecutionContext;

    @Inject
    public LogRepository
    (
            EbeanConfig ebeanConfig,
            EbeanDynamicEvolutions ebeanDynamicEvolutions,
            DatabaseExecutionContext databaseExecutionContext
    )
    {
        this.ebeanDynamicEvolutions = ebeanDynamicEvolutions;
        this.db = Ebean.getServer(ebeanConfig.defaultServer());
        this.databaseExecutionContext = databaseExecutionContext;
    }

    public CompletionStage<Request> saveRequest(String method, Http.Request request, Map requestHeaders)
    {
        return CompletableFuture.supplyAsync(() -> {
            Request apiRequest = new Request();
            apiRequest.setMethod(method);
            apiRequest.setPayload(request.body().asJson());
            apiRequest.setHeaders(Json.toJson(requestHeaders));
            apiRequest.setReceivedAt(System.currentTimeMillis());

            try
            {
                this.db.save(apiRequest);
                return apiRequest;
            }
            catch(Exception ex)
            {
                String message = "DB Interaction Failed. Exception: " + ex;
                throw new DBInteractionException(400, message);
            }
        }, this.databaseExecutionContext);
    }

    public void clear()
    {
        try
        {
            this.db.deleteAll(this.db.find(Request.class).findList());
        }
        catch(Exception ex)
        {
            String message = "DB Interaction Failed. Exception: " + ex;
            throw new DBInteractionException(400, message);
        }
    }
}
