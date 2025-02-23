package br.com.nlw.events.service;


import br.com.nlw.events.Exception.EventNotFoundException;
import br.com.nlw.events.Exception.SubscriptionConflictException;
import br.com.nlw.events.Exception.UserIndicadorNotFoundExpetion;
import br.com.nlw.events.dto.SubscriptionRankingByUser;
import br.com.nlw.events.dto.SubscriptionRankingItem;
import br.com.nlw.events.dto.SubscriptionResponse;
import br.com.nlw.events.model.Event;
import br.com.nlw.events.model.Subscription;
import br.com.nlw.events.model.User;
import br.com.nlw.events.repository.EventRepo;
import br.com.nlw.events.repository.SubscriptionRepo;
import br.com.nlw.events.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class SubscriptionService {

    @Autowired
    private EventRepo evtRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SubscriptionRepo subRepo;

    public SubscriptionResponse createNewSubscription(String eventName , User user , Integer userId){

    //recuperar o evento pelo nome
        Event evt = evtRepo.findByPrettyName(eventName);
        if (evt == null){ //caso alternativo 2
            throw new EventNotFoundException("Evento " + eventName + " não existe");
        }
        User userRec = userRepo.findByEmail(user.getEmail());
        if (userRec == null) { //caso alternativo 1
            userRec = userRepo.save(user);
        }

        User indicador =  null;
        if (userId != null){
         indicador = userRepo.findById(userId).orElse(null);
        if (indicador == null) {

            throw new UserIndicadorNotFoundExpetion("Usuário" + userId + "indicador não existe ");
            }
        }

        Subscription subs = new Subscription();
        subs.setEvent(evt);
        subs.setSubscriber(userRec);
        subs.setIndication(indicador);

        Subscription tmpSub = subRepo.findByEventAndSubscriber(evt,userRec);
        if (tmpSub != null) { //caso alternativo 3
            throw  new SubscriptionConflictException("Ja existe inscrição para o usuário " + userRec.getName() + " no evento " + evt.getTitle());
        }
        Subscription res = subRepo.save(subs);
        return  new SubscriptionResponse(res.getSubscriptionNumber() , "http://codecraft.com/subscription/" + res.getEvent().getPrettyName() + "/" + res.getSubscriber().getId());

    }

    public List<SubscriptionRankingItem> getCompleteRanking(String  prettyName){
        Event evt = evtRepo.findByPrettyName(prettyName);
        if (evt == null) {
            throw  new EventNotFoundException( "Ranking do evento " +prettyName + "não existe");
        }
        return subRepo.generateRanking(evt.getEventID());
    }

    public SubscriptionRankingByUser getRankingByUser( String prettyName, Integer userId){
        List<SubscriptionRankingItem> ranking = getCompleteRanking(prettyName);

        SubscriptionRankingItem item = ranking.stream().filter(i-> i.userId().equals(userId)).findFirst().orElse(null);
        if (item == null) {
            throw  new UserIndicadorNotFoundExpetion(" Não há inscrições com indicação do usuário " + userId);
        }

        Integer posicao = IntStream.range(0, ranking.size())
                .filter(pos -> ranking.get(pos).userId().equals(userId))
                .findFirst().getAsInt();

        return  new SubscriptionRankingByUser(item , posicao+1);

    }


}
