package utils

import com.ganesh.divekar.localdata.models.ComicsLocal


class TestDataContainer {

    companion object {
        fun getComics(): ComicsLocal {
            return ComicsLocal(
                "143",
                "Fake title 1",
                "For Doctor Bruce Banner life is anything but normal. But what happens when two women get between him and his alter ego, the Incorrigible Hulk? Hulk confused! \\r\\nIndy superstar Peter Bagge (THE MEGALOMANIACAL SPIDER-MAN) takes a satirical jab at the Hulk mythos with a tale of dames, debauchery and destruction.",
                "thumbnail.jpg",
                listOf("image.jg"),
                true
            )
        }

        fun getComicsList() =
            listOf(getComics(), getComics().copy(id = "123"), getComics().copy(id = "124"))


    }
}
