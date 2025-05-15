package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "requests")
@Getter
@Setter
public class Request extends Model
{
    @Id
    private Long id;

    private String method;

    private JsonNode payload;

    private JsonNode headers;

    private long receivedAt;
}
