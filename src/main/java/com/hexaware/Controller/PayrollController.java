package com.hexaware.Controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hexaware.Entity.Audit;
import com.hexaware.Entity.Payroll;
import com.hexaware.Service.PayrollService;

import java.util.List;

@RestController
@RequestMapping("/api/")
@CrossOrigin("http://localhost:3000")
public class PayrollController {

    @Autowired
    private PayrollService payService;

    //remove payroll
    @DeleteMapping("/payroll/remove_payroll/{pId}")
    public ResponseEntity<String> removePayroll(@PathVariable int pId) {
    	payService.removePayroll(pId);
        return ResponseEntity.ok("Payroll record with ID " + pId + " has been deleted successfully.");
    }

    //update payroll
    @PutMapping("/payroll/update_payroll/{pId}")
    public ResponseEntity<Payroll> updatePayroll(@PathVariable int pId, @RequestBody Payroll p) {
        Payroll updatedPayroll = payService.updatePayroll(pId, p);
        return new ResponseEntity<>(updatedPayroll, HttpStatus.OK);
    }

    //calculate payroll
    @PostMapping("/payroll/calculate_payroll/{empId}")
    public ResponseEntity<Payroll> calculatePayroll(@RequestBody Payroll p, @PathVariable int empId) {
        Payroll calculatedPayroll = payService.calculatePayroll(p, empId);
        return new ResponseEntity<>(calculatedPayroll, HttpStatus.OK);
    }

    //find by payroll ID
    @GetMapping("/payroll/get_payrollById/{pId}")
    public ResponseEntity<Payroll> findPayrollById(@PathVariable int pId) {
        Payroll payroll = payService.findByPayrollId(pId);
        return new ResponseEntity<>(payroll, HttpStatus.OK);
    }

    //find payroll by employee ID
    @GetMapping("/payroll/get_payrollByEmployeeId/{empId}")
    public ResponseEntity<List<Payroll>> findPayrollsByEmployeeId(@PathVariable int empId) {
        List<Payroll> payrolls = payService.findPayrollsByEmployeeId(empId);
        return new ResponseEntity<>(payrolls, HttpStatus.OK);
    }
    
    //get payroll audit logs
    @GetMapping("/payroll/audit")
    public List<Audit> getPayrollAuditLogs() {
        return payService.getPayrollAuditLogs();
    }
}
