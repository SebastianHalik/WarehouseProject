package API;

import Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
public class WareHouseController {

    @PostMapping("/api/product") //LP1
    public ResponseEntity<Object> addItem(@RequestBody Item item, @RequestParam int warehouse_id) throws FulfillmentNotFoundException,ItemNotFoundException {
        if (FulfillmentCenter.AddProductByID(warehouse_id, item)) {
            FulfillmentCenter.AddProductByID(warehouse_id, item);
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else if(item.getId()<0 & item.getName().isEmpty() & item.getBought()<0 & item.getPrice()<=0 & item.getAmount()<0){
            throw new ItemBadRequestException();
        }
        else{
            throw new FulfillmentNotFoundException();
        }
    }

    @DeleteMapping("/api/product/{item_id}") //LP2
    public ResponseEntity<Object> deleteItem(@PathVariable int item_id) throws ItemNotFoundException {
        if (FulfillmentCenter.removeItemByID(item_id)) {
            FulfillmentCenter.removeItemByID(item_id);
            return new ResponseEntity<>("Deleted item with id:  " + item_id, HttpStatus.OK);
        } else {
            throw new ItemNotFoundException();
        }
    }


    @GetMapping("/api/product/{item_id}/rating") // LP3
    public ResponseEntity<Object> getAverageRating(@PathVariable int item_id) throws ItemNotFoundException {
        if (FulfillmentCenter.getAverage(item_id) != 0)
            return new ResponseEntity<>(FulfillmentCenter.getAverage(item_id), HttpStatus.OK);

        else {
            throw new ItemNotFoundException();
        }
    }


    @GetMapping("/api/product/csv") //LP4
    public void exportCSV(HttpServletResponse response) throws Exception {
        //  CSV.WriteAllItemsToCSV();  //ALTERNATIVE WITHOUT DOWNLOAD
       CSV.DownloadCSV(response);
    }

    @GetMapping("/api/fulfillment") // LP5
    public ResponseEntity<Object> getWarehouses() {
        return new ResponseEntity<>(FulfillmentCenterContainer.WareHouseList,HttpStatus.OK);
    }

    @PostMapping("/api/fulfillment") //LP6
    public ResponseEntity<Object> addWarehouse(@RequestBody FulfillmentCenter fulfillmentCenter) throws FulfillmentBadRequestException {
        if (fulfillmentCenter.getWarehouseName() != null && fulfillmentCenter.getPlace() != null & fulfillmentCenter.getId() > 0 & fulfillmentCenter.getMaxSpace() > 0) {
            FulfillmentCenterContainer.WareHouseList.add(fulfillmentCenter);
            return new ResponseEntity<>(fulfillmentCenter, HttpStatus.OK);
        } else {
            throw new FulfillmentBadRequestException();
        }
    }

    @DeleteMapping("/api/fulfillment/{id}") //LP7
    public ResponseEntity<Object> deleteWarehouse(@PathVariable int id) {
        if (FulfillmentCenterContainer.deleteWH(id)) {
            FulfillmentCenterContainer.deleteWH(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            throw new FulfillmentNotFoundException();
        }
    }

    @GetMapping("/api/fulfillment/{id}/products") //LP8
    public ResponseEntity<Object> getAllItems(@PathVariable int id) {
        FulfillmentCenter fulfillmentCenter = FulfillmentCenterContainer.searchID(id);
        if (fulfillmentCenter != null)
            return new ResponseEntity<>(fulfillmentCenter.getProductList(), HttpStatus.OK);
        else
            throw new FulfillmentNotFoundException();

    }

    @GetMapping("/api/fulfillment/{wh_id}/fill") //LP9
    public ResponseEntity<Object> getPercent(@PathVariable ("wh_id") int wh_id) throws FulfillmentNotFoundException {
        FulfillmentCenter ful=FulfillmentCenterContainer.searchID(wh_id);
        assert ful != null;
        if (ful.getPercent(ful) >= 0)
            return new ResponseEntity<>(ful.getPercent(ful), HttpStatus.OK);
        else
            throw new FulfillmentNotFoundException();
    }

    @PostMapping("/api/rating") //LP10
    public ResponseEntity<Object> addRating(@RequestBody Rating rating, @RequestParam int item_id) throws ItemNotFoundException,RatingBadRequestException {
        if(FulfillmentCenter.addRating(rating,item_id))
            return new ResponseEntity<>(FulfillmentCenter.addRating(rating,item_id),HttpStatus.OK);
        else if(rating.getMark()>5 & rating.getMark()<0 & rating.getDescription().isEmpty()){
            throw new RatingBadRequestException();
        }
        else {
            throw new ItemNotFoundException();
        }
    }

}
