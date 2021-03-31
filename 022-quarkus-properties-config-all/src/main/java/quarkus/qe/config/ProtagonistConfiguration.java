package quarkus.qe.config;

import quarkus.qe.config.interfaces.ProtagonistConfigurable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProtagonistConfiguration {

    @Inject
    ProtagonistConfigurable protagonistConfigurable;

    public String getName(){
        return protagonistConfigurable.name();
    }

    public String getMessage(){
        return protagonistConfigurable.message();
    }

    public String getHobby() { return protagonistConfigurable.hobby(); }

    public String getFriendName(){ return protagonistConfigurable.friend().name(); }

    public String getFriendMessage(){
        return protagonistConfigurable.friend().message();
    }
}