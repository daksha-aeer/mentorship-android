package org.systers.mentorship.view.adapters

import android.app.AlertDialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.task_list_item.view.*
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.models.Task

/**
 * This class represents the adapter that fills in each view of the Tasks recyclerView
 * @param taskstsList list of tasks taken up by the mentee
 * @param markTask function to be called when an item from Tasks list is clicked
 */
class TasksAdapter(
        private val context: Context,
        private val tasksList: List<Task>,
        private val markTask: (taskId: Int) -> Unit
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
            TaskViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.task_list_item, parent, false))

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = tasksList[position]
        val itemView = holder.itemView

        itemView.cbTask.text = item.description
        /* Working: marking relation tasks as complete
        1. TaskService.kt makes PUT request to mentorship_relation/{request_id}/task/{task_id}/complete endpoint
        2. completeTask() in TaskDataManager.kt calls TaskService.kt and reads response as <CustomResponse>
        3. updateTask() in TaskViewModel.kt subscribes to the response and performs exception handling
        4. TasksAdapter.kt sets a listener for CheckBox cbTask. It displays an alert dialog for marking request
        as complete. It calls markTask() in TasksFragment.kt which in turn calls updateTask() and makes request.
         */
        itemView.cbTask.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.mark_task_title))
            builder.setMessage(context.getString(R.string.mark_task_message))
            builder.setPositiveButton(context.getString(R.string.yes)){dialog, which ->
                itemView.cbTask.isChecked=true
                markTask(item.id)
            }
            builder.setNegativeButton(context.getString(R.string.no)){dialog,which ->
                itemView.cbTask.isChecked=false
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun getItemCount(): Int = tasksList.size

    /**
     * This class holds a view for each item of the Tasks list
     * @param itemView represents each view of Tasks list
     */
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}