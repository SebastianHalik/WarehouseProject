package Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = FulfillmentNotFoundException.class)
    public static ResponseEntity<Object> NotFoundExceptionWH(){
        return new ResponseEntity<>("Fulfillment not found- error 404", HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = FulfillmentBadRequestException.class)
    public ResponseEntity<Object> BadRequestExceptionWH(){
        return new ResponseEntity<>("Fulfillment Bad Request- error 400", HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ItemBadRequestException.class)
    public ResponseEntity<Object> BadRequestExceptionITEM(){
        return new ResponseEntity<>("Item Bad Request- error 400", HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = ItemNotFoundException.class)
    public ResponseEntity<Object> NotFoundExceptionITEM(){
        return new ResponseEntity<>("ITEM not found- error 404", HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RatingBadRequestException.class)
    public ResponseEntity<Object> BadRequestRATING(){
        return new ResponseEntity<>("Rating Bad Request - error 400", HttpStatus.BAD_REQUEST);
    }

}
