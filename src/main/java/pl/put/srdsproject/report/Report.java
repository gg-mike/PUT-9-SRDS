package pl.put.srdsproject.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.put.srdsproject.fulfilled.FulfilledReport;
import pl.put.srdsproject.inventory.InventoryReport;
import pl.put.srdsproject.request.RequestReport;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    FulfilledReport fulfilledReport;
    InventoryReport inventoryReport;
    RequestReport requestReport;
}
