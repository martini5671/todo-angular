import {Component, inject, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';

export interface RemoveTask {
  taskId: number;
}

@Component({
  selector: 'remove-task-dialog',
  templateUrl: 'dialog.component.html',
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButton
  ]
})
export class RemoveTaskDialog {

  constructor(
    public dialogRef: MatDialogRef<RemoveTaskDialog>,
    @Inject(MAT_DIALOG_DATA) public data: RemoveTask) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onConfirm(): void {
    console.log("removing task: " + this.data.taskId);
    this.dialogRef.close(true); // ✅ user confirmed
  }

}
