package com.eleodoro.dev.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class GitRepositorio
{
    private String title;
    private String content;
    private String imgUrl;

    public GitRepositorio(String title, String content) {
        this.title = title;
        this.content = content;

        if(content.contains("![image]"))
        {
            Pattern pattern = Pattern.compile("!\\[image](.*?)\\n");
            Matcher matcher = pattern.matcher(content);
            matcher.results().findFirst().ifPresentOrElse(it->{this.imgUrl=it.group();},()->{this.imgUrl="";});
            this.content = content.replaceAll("!\\[image](.*?)\\n","").replaceFirst("\n","");
            if(this.imgUrl !="")
            {
                pattern = Pattern.compile("\\(([^)]+)\\)");
                matcher = pattern.matcher(this.imgUrl);
                this.imgUrl  = matcher.results().findFirst().get().group().replace("(","").replace(")","");

            }
        }else {
            this.imgUrl = "";
        }

    }
}
