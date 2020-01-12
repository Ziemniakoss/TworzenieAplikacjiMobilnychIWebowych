package com.ziemniak.bibliomobile.repositories

import com.ziemniak.bibliomobile.models.File

interface FilesRepositoryEventListener {
	fun filesFetched(files: Collection<File>)
	fun fileFetched(file: File)
}