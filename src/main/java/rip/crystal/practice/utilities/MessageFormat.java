package rip.crystal.practice.utilities;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import rip.crystal.practice.utilities.chat.CC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
public class MessageFormat {

    private String message;
    private List<String> messages;
    private HashMap<String, String> variables = Maps.newHashMap();

    public MessageFormat(Object object) {
        if (object instanceof List) messages = (List<String>) object;
        else message = (String) object;
    }

    public void setMessage(Object object) {
        if (object instanceof List) messages = (List<String>) object;
        else message = (String) object;
    }

//    public MessageFormat(List<String> messages){
//        this.message = null;
//        this.messages = messages;
//    }
//
//    public MessageFormat(String message){
//        this.message = message;
//        this.messages = null;
//    }

    public MessageFormat add(String variable, String value){
        variables.put(variable.toLowerCase(), value);
        return this;
    }

    public void send(CommandSender sender){
        if (messages != null) {
            messages.forEach(formatted -> {
                for (Map.Entry<String, String> entry : variables.entrySet()){
                    String variable = entry.getKey();
                    String value = entry.getValue();

                    formatted = formatted.replace(variable, value);
                }

                sender.sendMessage(CC.translate(formatted));
            });
        }
        else if (message != null) {
            String formatted = message;

            for (Map.Entry<String, String> entry : variables.entrySet()){
                String variable = entry.getKey();
                String value = entry.getValue();

                formatted = formatted
                        .replace(variable, value);
            }

            sender.sendMessage(CC.translate(formatted));
        }
    }

    public void broadcast(){
        if(message == null && messages != null){
            messages.forEach(formatted -> {
                for (Map.Entry<String, String> entry : variables.entrySet()){
                    String variable = entry.getKey();
                    String value = entry.getValue();

                    formatted = formatted
                            .replace(variable, value);
                }
                /*for (String string : formatted.split(" ")) {
                    if (string.contains("{") && variables.containsKey(getVariable(string).toLowerCase())) {
                        formatted = formatted
                            .replace(getVariable(string), variables.get(getVariable(string).toLowerCase()));
                    }
                }*/
                Bukkit.broadcastMessage(CC.translate(formatted));
            });
            return;
        }
        if(message != null){
            String formatted = message;
            for (Map.Entry<String, String> entry : variables.entrySet()){
                String variable = entry.getKey();
                String value = entry.getValue();

                formatted = formatted
                        .replace(variable, value);
            }
            /*for (String string : formatted.split(" ")) {
                if (string.contains("{") && variables.containsKey(getVariable(string).toLowerCase())) {
                    formatted = formatted
                        .replace(getVariable(string), variables.get(getVariable(string).toLowerCase()));
                }
            }*/
            Bukkit.broadcastMessage(CC.translate(formatted));
        }
    }

    public List<String> toList() {
        List<String> lines = Lists.newArrayList();
        if(messages != null){
            messages.forEach(formatted -> {
                for (Map.Entry<String, String> entry : variables.entrySet()){
                    String variable = entry.getKey();
                    String value = entry.getValue();

                    formatted = formatted
                            .replace(variable, value);
                }
                /*for (String string : formatted.split(" ")) {
                    if (string.contains("{") && variables.containsKey(getVariable(string).toLowerCase())) {
                        formatted = formatted
                            .replace(getVariable(string), variables.get(getVariable(string).toLowerCase()));
                    }
                }*/
                lines.add(CC.translate(formatted));
            });
        }
        return lines;
    }

    @Override
    public String toString() {
        if(message != null){
            String formatted = message;
            for (Map.Entry<String, String> entry : variables.entrySet()){
                String variable = entry.getKey();
                String value = entry.getValue();

                formatted = formatted
                        .replace(variable, value);
            }
            /*for (String string : formatted.split(" ")) {
                if (string.contains("{") && variables.containsKey(getVariable(string).toLowerCase())) {
                    formatted = formatted
                        .replace(getVariable(string), variables.get(getVariable(string).toLowerCase()));
                }
            }*/
            return CC.translate(formatted);
        }
        return "";
    }

    private String getVariable(String string){
        if (string == null) return  "";
        if (string.contains("{")) {
            StringBuilder variable = new StringBuilder();
            boolean add = false;
            for (char s : string.toCharArray()){
                if(s == '{') add = true;
                if(s == '}') {
                    variable.append(s);
                    break;
                }
                if (add) variable.append(s);
            }
            return variable.toString();
        }
        return string;
    }
}