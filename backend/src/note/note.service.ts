import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/sequelize';
import { Note } from './note.model';
import { NewNoteDto } from './dto/newNote.dto';
import { UpdateNoteDto } from './dto/updateNote.dto';
import { Tag } from 'src/tag/tag.model';

@Injectable()
export class NoteService {
  constructor(
    @InjectModel(Note)
    private noteModel: typeof Note,

    @InjectModel(Tag) // Inject the Tag model
    private tagModel: typeof Tag,
  ) {}

  async create(username: string, newNoteDto: NewNoteDto) {
    // Extract tags from the newNoteDto
    const { tags, ...noteData } = newNoteDto;

    // Create the note
    const note = await this.noteModel.create({
      ...noteData,
      username,
    });

    // Associate tags with the note
    if (tags && tags.length > 0) {
      await note.$set('tags', tags);
    }

    return note;
  }

  async fetchAllForUser(username: string) {
    return this.noteModel.findAll({
      where: {
        username,
      },
      include: 'tags', // Include tags in the result
    });
  }

  async delete(id: string | number) {
    await this.noteModel.destroy({ where: { id } });
  }

  async updateasdsad(dto: UpdateNoteDto, id: string | number) {
    await this.noteModel.update(dto, { where: { id } });
    return this.noteModel.findByPk(id);
  }

  async update(dto: UpdateNoteDto, id: string | number) {
    // Update the note's basic fields
    await this.noteModel.update(dto, { where: { id } });

    // Find the note
    const note = await this.noteModel.findByPk(id, { include: 'tags' });

    if (note) {
      // Extract tags from the dto
      const { tags } = dto;

      // Assuming tags are strings, find or create Tag instances
      const tagInstances = await Promise.all(
        tags.map(tagName =>
          this.tagModel.findOrCreate({ where: { name: tagName } })
            .then(([tag]) => tag) // Extract Tag instance from findOrCreate response
        )
      );

      // Associate these tags with the note
      await note.$set('tags', tagInstances);
    }

    return this.noteModel.findByPk(id, { include: 'tags' }); // Return note with tags
  }

}
