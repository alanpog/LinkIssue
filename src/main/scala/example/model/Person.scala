package example.model

import net.liftweb.mapper.{LongKeyedMetaMapper, IdPK, MappedString, LongKeyedMapper}

class Person extends LongKeyedMapper[Person] with IdPK {
  def getSingleton = Person
  object fullName extends MappedString(this, 30)
  object email extends MappedString(this, 30)
}

object Person extends Person with LongKeyedMetaMapper[Person]