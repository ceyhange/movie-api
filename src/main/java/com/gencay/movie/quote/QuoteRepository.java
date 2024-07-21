package com.gencay.movie.quote;

import com.gencay.movie.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    Quote findByRole(String role);
    Quote findByShow(Movie show);

}
