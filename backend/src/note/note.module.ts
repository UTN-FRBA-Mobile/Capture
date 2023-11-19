import { Module } from '@nestjs/common';
import { SequelizeModule } from '@nestjs/sequelize';
import { Note } from './note.model';
import { Tag } from '../tag/tag.model'; // Import the Tag model
import { NoteService } from './note.service';
import { NoteController } from './note.controller';

@Module({
  imports: [
    SequelizeModule.forFeature([Note, Tag]), // Include Tag model here
  ],
  providers: [NoteService],
  controllers: [NoteController],
})
export class NoteModule {}
