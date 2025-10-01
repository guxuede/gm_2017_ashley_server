package entityEdit;

import com.badlogic.ashley.core.ComponentMapper;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.NetClientComponent;

/**
 * Created by guxuede on 2017/5/31 .
 */
public class Mappers {
    public static final ComponentMapper<ChannelComponent> channelCM = ComponentMapper.getFor(ChannelComponent.class);
    public static final ComponentMapper<NetClientComponent> netPackCM = ComponentMapper.getFor(NetClientComponent.class);



}
