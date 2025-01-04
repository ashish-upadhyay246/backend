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
    private PayrollService pser;

    //remove payroll
    @DeleteMapping("/payroll/remove_payroll/{pId}")
    public ResponseEntity<String> removePayroll(@PathVariable int pId) {
    	pser.removePayroll(pId);
        return ResponseEntity.ok("Payroll record with ID " + pId + " has been deleted successfully.");
    }

    //update payroll
    @PutMapping("/payroll/update_payroll/{pId}")
    public ResponseEntity<Payroll> updatePayroll(@PathVariable int pId, @RequestBody Payroll p) {
        Payroll updatedPayroll = pser.updatePayroll(pId, p);
        return new ResponseEntity<>(updatedPayroll, HttpStatus.OK);
    }

    //calculate payroll
    @PostMapping("/payroll/calculate_payroll/{empId}")
    public ResponseEntity<Payroll> calculatePayroll(@RequestBody Payroll p, @PathVariable int empId) {
        Payroll calculatedPayroll = pser.calculatePayroll(p, empId);
        return new ResponseEntity<>(calculatedPayroll, HttpStatus.OK);
    }

    //find by payroll ID
    @GetMapping("/payroll/get_payrollById/{pId}")
    public ResponseEntity<Payroll> findPayrollById(@PathVariable int pId) {
        Payroll payroll = pser.findByPayrollId(pId);
        return new ResponseEntity<>(payroll, HttpStatus.OK);
    }

    //find payroll by employee ID
    @GetMapping("/payroll/get_payrollByEmployeeId/{empId}")
    public ResponseEntity<List<Payroll>> findPayrollsByEmployeeId(@PathVariable int empId) {
        List<Payroll> payrolls = pser.findPayrollsByEmployeeId(empId);
        return new ResponseEntity<>(payrolls, HttpStatus.OK);
    }
    
    //get payroll audit logs
    @GetMapping("/payroll/audit")
    public List<Audit> getPayrollAuditLogs() {
        return pser.getPayrollAuditLogs();
    }
}
