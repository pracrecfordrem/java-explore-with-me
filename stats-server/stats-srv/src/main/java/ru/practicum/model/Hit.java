package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hit implements Comparable<Hit> {
    private String app;
    private String uri;
    private Long hits;

    @Override
    public int compareTo(Hit o) {
        if (o.hits.equals(this.hits)) {
            return 0;
        }
        return o.hits > this.hits ? 1 : -1;
    }
}
