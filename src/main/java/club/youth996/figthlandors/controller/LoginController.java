package club.youth996.figthlandors.controller;

import club.youth996.figthlandors.entity.*;
import club.youth996.figthlandors.service.UserInfoService;
import club.youth996.figthlandors.service.UserService;
import club.youth996.figthlandors.util.CardPlay;
import club.youth996.figthlandors.util.JsonChange;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhan Xinjian
 * @date 2020/4/17 18:07
 * <p></p>
 */
@Slf4j
@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;

    @RequestMapping("/")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/start")
    public String start() {
        return "start";
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public String login(String username, String password, HttpSession session) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().select("username", "password").eq("username", username).eq("password", password);
        User user = userService.getOne(wrapper);
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("Account",username);
            return "start";
        } else {
            return "用户名或密码错误！";
        }
    }

    @ResponseBody
    @RequestMapping("/registerUser")
    public String registerUser(String username,String password,String nickname,String sex) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().select("username").eq("username", username);
        User one = userService.getOne(wrapper);
        if (null != one) {
            return "已存在的用户名";
        }
        else{
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            Integer i = "男".equals(sex) ? 0 : 1;
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(username);
            userInfo.setNickname(nickname);
            userInfo.setSex(i);
            boolean b = false;
            boolean b1 = false;
            synchronized (this) {
                b = userService.save(user);
                b1 = userInfoService.save(userInfo);
            }
            if (b&&b1) {
                return "注册成功";
            }else{
                return "注册失败";
            }
        }

    }

    @RequestMapping("/rank")
    public String rank(@RequestParam(name = "pageNo")String pageNo, Model model) {
        Page<UserInfo> userInfoPage = new Page<>(Integer.parseInt(pageNo), 10);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<UserInfo>().orderByDesc("score");
        Page<UserInfo> page = userInfoService.page(userInfoPage, wrapper);
        model.addAttribute("page", page);
        return "rank";
    }

    /**
     * 初始化游戏
     *
     * @param request
     * @return
     */
    @RequestMapping("/initGames")
    @ResponseBody
    public String init(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        CardGames cardGames = new CardGames();
        request.getSession().setAttribute("cardGames", cardGames);
        CardGames.creatCardGame(user);
        int cardGameID = user.getCardGameID();
        Map<Integer, CardGame> games = cardGames.getCardGames();
        CardGame cardGame = games.get(cardGameID);
        Map<String, Object> map = CardPlay.begin(cardGame);
        return JSON.toJSONString(map);
    }

    /**
     * 判断用户行为
     *
     * @param request
     * @return
     */
    @RequestMapping("/cardJudge")
    @ResponseBody
    public String cardJudge(HttpServletRequest request) {
        String[] strs = request.getParameterValues("list");

        List<Card> list = JsonChange.toList(strs);
        String result;
        int f;
        User user = (User) request.getSession().getAttribute("user");
        CardGames cardGames = (CardGames) request.getSession().getAttribute("cardGames");

        log.info(user.toString());
        int cardGameID = user.getCardGameID();
        Map<Integer, CardGame> games = cardGames.getCardGames();
        CardGame cardGame = games.get(cardGameID);
        System.out.println("cardGame" + cardGame.getId());
        //点击不出
        int role0 = 0;
        int role1 = 1;
        int role2 = 2;
        if (list == null) {
            cardGame.getFlag()[role1] = false;
            cardGame.getCurrentList()[role1] = list;
            if (cardGame.getFlag()[role0] == false && cardGame.getFlag()[role2] == false) {
                //必须出牌不能不出
                result = "0";
            } else {
                result = "1";
            }

        } else {
            //玩家主动出牌
            if (cardGame.getFlag()[role0] == false && cardGame.getFlag()[role2] == false) {
                //System.out.println(cardGame.getId()+"主动出牌");
                CardType re = CardPlay.jugdeType(list);
                //System.out.println(cardGame.getId()+"我的牌型" + re);
                if (re == CardType.c0) {
                    f = 0;
                } else {
                    f = 1;
                }
            } else {
                //System.out.println(cardGame.getId()+"跟牌");
                f = CardPlay.checkCards(list, cardGame.getCurrentList());
            }
            if (f == 1) {
                result = "1";
                cardGame.getFlag()[role1] = true;
                cardGame.getCurrentList()[role1] = list;
            } else {
                cardGame.getFlag()[role1] = false;
                result = "0";
            }
        }
        return result;
    }

    @RequestMapping("/robotPlaying")
    @ResponseBody
    public String robotPlaying(HttpServletRequest request) {
        String judgement = request.getParameter("Judgement");
        List<Card> re = null;
        log.info("judgement:" + judgement);

        User user = (User) request.getSession().getAttribute("user");
        CardGames cardGames = (CardGames) request.getSession().getAttribute("cardGames");

        log.info(user.toString());
        int cardGameID = user.getCardGameID();
        Map<Integer, CardGame> games = cardGames.getCardGames();
        CardGame cardGame = games.get(cardGameID);
        String robot2 = "2";
        String robot0 = "0";
        //2号人机出牌
        if (robot2.equals(judgement)) {
            re = JsonChange.toCard(CardPlay.comptuerShowCards(2, cardGame));
            if (re.isEmpty()) {
                cardGame.getFlag()[2] = false;
                cardGame.getCurrentList()[2] = re;
            } else {
                cardGame.getFlag()[2] = true;
                cardGame.getCurrentList()[2] = re;
            }
        }
        //0号人机出牌
        else if (robot0.equals(judgement)) {
            re = JsonChange.toCard(CardPlay.comptuerShowCards(0, cardGame));
            if (re.isEmpty()) {
                cardGame.getFlag()[0] = false;
                cardGame.getCurrentList()[0] = re;
            } else {
                cardGame.getFlag()[0] = true;
                cardGame.getCurrentList()[0] = re;
            }
        }
        return JSON.toJSONString(re);
    }

    /**
     * 判断谁抢到了地主
     *
     * @return
     */
    @RequestMapping("/landorJudge")
    @ResponseBody
    public String landorJudge(HttpServletRequest request) {
        String judgement = request.getParameter("Judgement");
        String result = null;
        List<Card> robot0 = null;
        List<Card> robot2 = null;
        Map<String, Object> map = new HashMap<>(16);
        User user = (User) request.getSession().getAttribute("user");
        CardGames cardGames = (CardGames)request.getSession().getAttribute("cardGames");

        log.info(user.toString());
        int cardGameID = user.getCardGameID();
        Map<Integer, CardGame> games = cardGames.getCardGames();
        CardGame cardGame = games.get(cardGameID);
        log.info(cardGame.toString());
        String flag = "1";
        if (flag.equals(judgement)) {
            CardPlay.jugdeLord(1, cardGame);
            map.put("playerList1", cardGame.getPlayerList()[1]);
            result = "1";
        } else {
            CardPlay.jugdeLord(0, cardGame);
            int f = cardGame.getLandLordsFlag();
            if (f == 0) {
                result = f + "";
                robot0 = JsonChange.toCard(CardPlay.comptuerShowCards(0, cardGame));
                cardGame.getFlag()[0] = true;
                cardGame.getCurrentList()[0] = robot0;
                robot2 = null;
            } else {
                result = f + "";
                robot2 = JsonChange.toCard(CardPlay.comptuerShowCards(2, cardGame));
                cardGame.getFlag()[2] = true;
                cardGame.getCurrentList()[2] = robot2;
                robot0 = JsonChange.toCard(CardPlay.comptuerShowCards(0, cardGame));
                if (robot0 == null) {
                    cardGame.getFlag()[0] = false;
                    cardGame.getCurrentList()[0] = robot0;
                    System.out.println("0不出");
                } else {
                    cardGame.getFlag()[0] = true;
                    cardGame.getCurrentList()[0] = robot0;
                }
            }
            map.put("a0", robot0);
            map.put("a2", robot2);
        }
        map.put("result", result);
        return JSON.toJSONString(map);
    }

    @RequestMapping("/gameOver")
    public void gameOver(HttpServletRequest request) {
        String judgement = request.getParameter("Judgement");
        //用户账号
        String account =(String) request.getSession().getAttribute("Account");
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<UserInfo>().eq("username", account);
        UserInfo userInfo = userInfoService.getOne(wrapper);
        String flag = "1";
        //获胜
        if(flag.equals(judgement)) {
            userInfo.setScore(userInfo.getScore()+1);
            userInfoService.update(userInfo, wrapper);
        }else {
            userInfo.setScore(userInfo.getScore()-1);
            userInfoService.update(userInfo, wrapper);
        }
    }
}