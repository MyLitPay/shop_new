package com.simbirsoft.mapper;

import com.simbirsoft.api.dto.InvoiceDto;
import com.simbirsoft.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvoiceMapper {

//    @Autowired
//    protected GroupRepository groupRepository;

    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

//    @Mapping(source = "group", target = "groupId", qualifiedByName = "toGroupId")
    @Mapping(target = "groupId", expression = "java(invoice.getGroup().getId())")
    InvoiceDto toDTO(Invoice invoice);

//    @Mapping(source = "groupId", target = "group", qualifiedByName = "toGroup")
    Invoice toEntity(InvoiceDto invoiceDto);

//    @Named("toGroupId")
//    public Long toGroupId(Group group) {
//        return group.getId();
//    }
//
//    @Named("toGroup")
//    public Group toGroup(Long groupId) {
//        System.out.println(1234567890);
//        System.out.println(groupId);
//        System.out.println(groupRepository.findById(1L));
//        System.out.println(groupRepository.findById(groupId));
//        return groupRepository.findById(groupId).orElse(new Group());
//    }
}
