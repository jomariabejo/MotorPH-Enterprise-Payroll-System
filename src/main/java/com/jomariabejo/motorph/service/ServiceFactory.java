package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.repository.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceFactory {
    private AnnouncementService announcementService = new AnnouncementService(new AnnouncementRepository());
    private BonusService bonusService = new BonusService(new BonusRepository());
    private ConversationService conversationService = new ConversationService(new ConversationRepository());
    private DepartmentService departmentService = new DepartmentService(new DepartmentRepository());
    private EmployeeService employeeService = new EmployeeService(new EmployeeRepository());
    private LeaveBalanceService leaveBalanceService = new LeaveBalanceService(new LeaveBalanceRepository());
    private LeaveRequestService leaveRequestService = new LeaveRequestService(new LeaveRequestRepository());
    private LeaveRequestTypeService leaveRequestTypeService = new LeaveRequestTypeService(new LeaveRequestTypeRepository());
    private MessageService messageService = new MessageService(new MessageRepository());
    private MessageAttachmentService messageAttachmentService = new MessageAttachmentService(new MessageAttachmentRepository());
    private MessageFolderService messageFolderService = new MessageFolderService(new MessageFolderRepository());
    private MessageStatusService messageStatusService = new MessageStatusService(new MessageStatusRepository());
    private NotificationService notificationService = new NotificationService(new NotificationRepository());
    private OvertimeRequestService overtimeRequestService = new OvertimeRequestService(new OvertimeRequestRepository());
    private PagibigContributionRateService pagibigContributionRateService = new PagibigContributionRateService(new PagibigContributionRateRepository());
    private PayrollService payrollService = new PayrollService(new PayrollRepository());
    private PayrollApprovalService payrollApprovalService = new PayrollApprovalService(new PayrollApprovalRepository());
    private PayrollChangeService payrollChangeService = new PayrollChangeService(new PayrollChangeRepository());
    private PayrollTransactionService payrollTransactionService = new PayrollTransactionService(new PayrollTransactionRepository());
    private PayslipService payslipService = new PayslipService(new PayslipRepository());
    private PayslipHistoryService payslipHistoryService = new PayslipHistoryService(new PayslipHistoryRepository());
    private PermissionService permissionService = new PermissionService(new PermissionRepository());
    private PhilhealthContributionRateService philhealthContributionRateService = new PhilhealthContributionRateService(new PhilhealthContributionRateRepository());;
    private PositionService positionService = new PositionService(new PositionRepository());
    private ReimbursementRequestService reimbursementRequestService = new ReimbursementRequestService(new ReimbursementRequestRepository());
    private ReimbursementTransactionService reimbursementTransactionService = new ReimbursementTransactionService(new ReimbursementTransactionRepository());
    private RoleService roleService = new RoleService(new RoleRepository());
    private RolePermissionService rolePermissionService = new RolePermissionService(new RolePermissionRepository());
    private SssContributionRateService sssContributionRateService = new SssContributionRateService(new SssContributionRateRepository());
    private TimesheetService timesheetService = new TimesheetService(new TimesheetRepository());
    private TinComplianceService tinComplianceService = new TinComplianceService(new TinComplianceRepository());
    private UserService userService = new UserService(new UserRepository());
    private UserLogService userLogService = new UserLogService(new UserLogRepository());
}
