package com.example.lifeCycle

object DataManager {
    val courses = HashMap<String, CourseInfo>()
    private val notes = ArrayList<NoteInfo>()

    init {
        initializeCourses()
        initializeNotes()
    }

    fun loadNotes(): List<NoteInfo> {
        simulateLoadDelay()
        return notes
    }

    fun loadNotes(vararg noteIds: Int): List<NoteInfo> {
        simulateLoadDelay()
        val noteList: List<NoteInfo>

        if(noteIds.isEmpty())
            noteList = notes
        else {
            noteList = ArrayList<NoteInfo>(noteIds.size)
            for(noteId in noteIds)
                noteList.add(notes[noteId])
        }
        return noteList
    }

    fun loadNote(noteId: Int) = notes[noteId]

    fun isLastNoteId(noteId: Int) = noteId == notes.lastIndex

    fun idOfNote(note: NoteInfo) = notes.indexOf(note)

    fun noteIdsAsIntArray(notes: List<NoteInfo>): IntArray {
        val noteIds = IntArray(notes.size)
        for(index in 0..notes.lastIndex)
            noteIds[index] = idOfNote(notes[index])
        return noteIds
    }

    fun addNote(course: CourseInfo, noteTitle: String, noteText: String): Int {
        return addNote(NoteInfo(course, noteTitle, noteText))
    }

    fun addNote(note: NoteInfo): Int{
        notes.add(note)
        return notes.lastIndex
    }

    fun findNote(course: CourseInfo, noteTitle: String, noteText: String) : NoteInfo? {
        for(note in notes)
            if (course == note.course &&
                    noteTitle == note.title &&
                    noteText == note.text)
                return note

        return null
    }

    private fun simulateLoadDelay() {
        Thread.sleep(1000)
    }

    fun initializeCourses() {
        var course = CourseInfo("android_intents", "Android Programming with Intents")
        courses.set(course.courseId, course)

        course = CourseInfo(courseId = "android_async", title = "Android Async Programming and Services")
        courses.set(course.courseId, course)

        course = CourseInfo(title = "Java Fundamentals: The Java Language", courseId = "java_lang")
        courses.set(course.courseId, course)

        course = CourseInfo("java_core", "Java Fundamentals: The Core Platform")
        courses.set(course.courseId, course)
    }

    fun initializeNotes() {

        var course = courses["android_intents"]!!
        var note = NoteInfo(
            course, "Dynamic intent resolution",
            "Wow, intents allow components to be resolved at runtime"
        )
        notes.add(note)
        note = NoteInfo(
            course, "Delegating intents",
            "PendingIntents are powerful; they delegate much more than just a component invocation"
        )
        notes.add(note)

        course = courses["android_async"]!!
        note = NoteInfo(
            course, "Service default threads",
            "Did you know that by default an Android Service will tie up the UI thread?"
        )
        notes.add(note)
        note = NoteInfo(
            course, "Long running operations",
            "Foreground Services can be tied to a notification icon"
        )
        notes.add(note)

        course = courses["java_lang"]!!
        note = NoteInfo(
            course, "Parameters",
            "Leverage variable-length parameter lists"
        )
        notes.add(note)
        note = NoteInfo(
            course, "Anonymous classes",
            "Anonymous classes simplify implementing one-use types"
        )
        notes.add(note)

        course = courses["java_core"]!!
        note = NoteInfo(
            course, "Compiler options",
            "The -jar option isn't compatible with with the -cp option"
        )
        notes.add(note)
        note = NoteInfo(
            course, "Serialization",
            "Remember to include SerialVersionUID to assure version compatibility"
        )
        notes.add(note)
    }

}