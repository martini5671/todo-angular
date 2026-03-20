export * from './taskController.service';
import { TaskControllerService } from './taskController.service';
export * from './taskController.serviceInterface';
export * from './userController.service';
import { UserControllerService } from './userController.service';
export * from './userController.serviceInterface';
export const APIS = [TaskControllerService, UserControllerService];
