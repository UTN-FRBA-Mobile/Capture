import { Module } from '@nestjs/common';
import { SequelizeModule } from '@nestjs/sequelize';
import { Tag } from '../tag/tag.model';
import { TagService } from './tag.service';

@Module({
  imports: [SequelizeModule.forFeature([Tag])],
  providers: [TagService],
  exports: [TagService],
})
export class TagModule {}
