package com.inspireme.presentationlayer.notfoundadvices;

        import com.inspireme.presentationlayer.notfoundexceptions.CommentNotFoundException;
        import com.inspireme.presentationlayer.notfoundexceptions.NoArticlesFoundInCategoryException;
        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ControllerAdvice;
        import org.springframework.web.bind.annotation.ExceptionHandler;
        import org.springframework.web.bind.annotation.ResponseBody;
        import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoArticlesFoundInCategoryAdvice {
    @ResponseBody
    @ExceptionHandler(NoArticlesFoundInCategoryException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String NoArticlesFoundInCategoryHandler(NoArticlesFoundInCategoryException ex) {
        return ex.getMessage();
    }
}
