package entityEdit;

import com.badlogic.ashley.core.ComponentMapper;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.MessageComponent;
import com.guxuede.gm.net.system.component.PlayerDataComponent;

/**
 * Created by guxuede on 2017/5/31 .
 */
public class Mappers {
    public static final ComponentMapper<ChannelComponent> channelCM = ComponentMapper.getFor(ChannelComponent.class);
    public static final ComponentMapper<MessageComponent> messageCM = ComponentMapper.getFor(MessageComponent.class);

    public static final ComponentMapper<PlayerDataComponent> playerCM = ComponentMapper.getFor(PlayerDataComponent.class);


}
