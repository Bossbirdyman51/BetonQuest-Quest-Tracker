package majster2nn.dev.betonQuestQT.Events;

import majster2nn.dev.betonQuestQT.Database.DataBaseManager;
import majster2nn.dev.betonQuestQT.Tracker.QuestPlaceholder;
import org.betonquest.betonquest.BetonQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class Events implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        BetonQuest.getInstance().getPackages().forEach((id, questPackage) -> {
            if(!questPackage.getTemplates().contains("trackedQuest")){return;}
            QuestPlaceholder.packageStatusesMap.computeIfAbsent(player, k -> new HashMap<>())
                    .put(questPackage, QuestPlaceholder.Statuses.valueOf(
                            DataBaseManager.getQuestStatus(player.getUniqueId().toString(), id).toUpperCase()
                    ));
        });
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();

        BetonQuest.getInstance().getPackages().forEach((id, questPackage) -> {
            if(!questPackage.getTemplates().contains("trackedQuest")){return;}
            DataBaseManager.setQuestPackage(id, player.getUniqueId().toString(), QuestPlaceholder.packageStatusesMap.get(player).getOrDefault(questPackage, QuestPlaceholder.Statuses.LOCKED));
        });
    }
}
