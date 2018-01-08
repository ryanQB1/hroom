package server.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import server.model.Oder;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-08T16:21:29")
@StaticMetamodel(Profile.class)
public class Profile_ { 

    public static volatile SingularAttribute<Profile, String> password;
    public static volatile SingularAttribute<Profile, Boolean> admin;
    public static volatile ListAttribute<Profile, Oder> orders;
    public static volatile SingularAttribute<Profile, String> Id;

}